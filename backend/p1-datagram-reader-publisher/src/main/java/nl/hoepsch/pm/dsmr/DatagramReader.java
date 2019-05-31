package nl.hoepsch.pm.dsmr;

import org.apache.commons.compress.utils.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
     * The datagram acceptor.
     */
    private final DatagramAcceptor datagramAcceptor;

    /**
     * The constructor.
     *
     * @param datagramAcceptor The datagram acceptor.
     */
    @Autowired
    public DatagramReader(final DatagramAcceptor datagramAcceptor) {
        this.datagramAcceptor = datagramAcceptor;
    }

    /**
     * Reads the input stream with the configured datagram acceptor.
     *
     * @param inputStream The input stream to read the datagrams from.
     * @throws IOException in case of read failures.
     */
    public void consume(final InputStream inputStream) throws IOException {
        consume(inputStream, datagramAcceptor);
    }

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
     * @param reader The reader to read the datagrams from.
     * @throws IOException in case of read failures.
     */
    public void consume(final BufferedReader reader) throws IOException {
        consume(reader, datagramAcceptor);
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
                if (NEWLINE == character) {
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
                        datagramAcceptor.accept(parse(datagramBuilder.toString(), checksumBuilder.toString()));
                    } catch (Throwable ignored) {
                        LOGGER.error("Caught exception:", ignored);
                    }
                } else {
                    checksumBuilder.append(character);
                }
            }

        }
    }
}