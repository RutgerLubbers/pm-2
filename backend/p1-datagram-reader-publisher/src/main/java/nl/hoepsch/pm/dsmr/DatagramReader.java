package nl.hoepsch.pm.dsmr;

import org.apache.commons.compress.utils.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static nl.hoepsch.pm.dsmr.extractor.DSMR5Parser.parse;

/**
 * The datagram reader parses datagrams from a stream and offers them to the datagram acceptor.
 */
@Component
public class DatagramReader {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DatagramReader.class);

    /**
     * Constant for reading a datagram. This is the start of a new datagram.
     */
    private static final char FORWARD_SLASH = '/';

    /**
     * Constant for reading a datagram. The last line of a datagram starts with the '!'.
     */
    private static final char EXCLAMATION_MARK = '!';

    /**
     * Constant for reading a datagram.
     */
    private static final char NEWLINE = '\n';

    /**
     * Constant for reading a datagram.
     */
    private static final char CARRIAGE_RETURN = '\r';


    /**
     * Reads the input stream with the given datagram acceptor.
     *
     * @param inputStream      The input stream to read the datagrams from.
     * @param datagramAcceptor The datagram acceptor to use.
     * @throws IOException in case of read failures.
     */
    public void consume(final InputStream inputStream, final DatagramAcceptor datagramAcceptor) throws IOException {
        consume(new BufferedReader(new InputStreamReader(inputStream, Charsets.UTF_8)), datagramAcceptor);
    }


    /**
     * Reads the datagrams from the reader with the configured datagram acceptor.
     *
     * @param reader           The reader to read the datagrams from.
     * @param datagramAcceptor The datagram acceptor.
     * @throws IOException in case of read failures.
     */
    @SuppressWarnings({"PMD.AvoidCatchingThrowable", "PMD.AvoidInstantiatingObjectsInLoops", "PMD.AssignmentInOperand"})
    public void consume(final BufferedReader reader, final DatagramAcceptor datagramAcceptor) throws IOException {
        boolean lastLineOfDatagram = false;
        StringBuilder datagramBuilder = new StringBuilder();
        StringBuilder checksumBuilder = new StringBuilder();
        char lastCharacter = '-';
        int charRead;
        while ((charRead = reader.read()) != -1) {
            final char character = (char) charRead;
            if (FORWARD_SLASH == character) {
                // New datagram
                lastLineOfDatagram = false;
                datagramBuilder = new StringBuilder();
                checksumBuilder = new StringBuilder();
            }

            if (!lastLineOfDatagram) {
                if (NEWLINE == character && lastCharacter != CARRIAGE_RETURN) {
                    // Append CR for files copied via SSH (automatic DOS -> Unix conversion)
                    datagramBuilder.append(CARRIAGE_RETURN);
                }
                if (EXCLAMATION_MARK == character) {
                    // Last line of datagram
                    lastLineOfDatagram = true;
                }

                datagramBuilder.append(character);
            } else {
                if (NEWLINE == character) {
                    lastLineOfDatagram = false;
                    try {
                        datagramAcceptor.accept(parse(datagramBuilder.toString(), getChecksum(checksumBuilder)));
                    } catch (Throwable ignored) {
                        LOGGER.error("Caught error.", ignored);
                    }
                } else {
                    checksumBuilder.append(character);
                }
            }

            lastCharacter = character;
        }
    }

    @SuppressWarnings("PMD.LawOfDemeter")
    private String getChecksum(final StringBuilder checksumBuilder) {
        final String checksum = checksumBuilder.toString();
        // Checksum might contain the '\r' character, strip it.
        return checksum.replace("\r", "");
    }
}
