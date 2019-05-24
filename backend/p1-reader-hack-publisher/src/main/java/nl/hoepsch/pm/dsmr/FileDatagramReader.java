package nl.hoepsch.pm.dsmr;

import nl.hoepsch.pm.DatagramAcceptor;
import nl.hoepsch.pm.dsmr.model.DSMR5Datagram;
import org.apache.commons.compress.utils.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static nl.hoepsch.pm.dsmr.extractor.DSMR5Parser.parse;

/**
 * Datagram reader to read datagrams from a ZIP or a directory containing ZIP files.
 */
@SuppressWarnings("PMD.DoNotCallSystemExit")
@Component
public class FileDatagramReader {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDatagramReader.class);

    /**
     * Log message in case of IO exceptions.
     */
    private static final String CAUGHT_ERROR = "Caught error.";

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
     * Read all zip files in a directory.
     *
     * @param directory The directory containing (zero or more) zip files.
     * @param acceptor  The acceptor for the parsed datagrams.
     */
    public void readAll(final String directory, final DatagramAcceptor acceptor) {
        final Path zipDir = Paths.get(directory);
        try {
            Files.list(zipDir).sorted().forEach(zipFile -> readZipFile(zipFile, acceptor));
        } catch (IOException ex) {
            LOGGER.error(CAUGHT_ERROR, ex);
        }
    }

    /**
     * Read a zip file.
     *
     * @param zipFile  The file to read.
     * @param acceptor The acceptor for the parsed datagrams.
     */
    public void readZipFile(final Path zipFile, final DatagramAcceptor acceptor) {
        try {
            final FileSystem fileSystem = FileSystems.newFileSystem(zipFile, FileDatagramReader.class.getClassLoader());
            for (final Path root : fileSystem.getRootDirectories()) {
                Files.walk(root).sorted().forEach(
                    zipFileEntry -> {
                        if (Files.isRegularFile(zipFileEntry)) {
                            try {
                                LOGGER.debug("Reading file '{}{}'.", zipFile.getFileName(), zipFileEntry);
                                final List<DSMR5Datagram> datagrams = readFile(zipFileEntry);
                                datagrams.forEach(acceptor::accept);
                                System.exit(1);
                            } catch (IOException e) {
                                LOGGER.error(CAUGHT_ERROR, e);
                            }
                        }
                    }
                );
            }
        } catch (IOException ex) {
            LOGGER.error(CAUGHT_ERROR, ex);
        }
    }

    private static List<DSMR5Datagram> readFile(final Path gzipFile) throws IOException {
        try (
            InputStream inputStream = Files.newInputStream(gzipFile);
            GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(gzipInputStream, Charsets.UTF_8))) {
            return readDatagrams(reader);
        }
    }

    @SuppressWarnings({"PMD.AvoidCatchingThrowable", "PMD.AvoidInstantiatingObjectsInLoops", "PMD.AssignmentInOperand"})
    private static List<DSMR5Datagram> readDatagrams(final BufferedReader reader) throws IOException {
        final List<DSMR5Datagram> contents = new ArrayList<>();

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
                        contents.add(parse(datagramBuilder.toString(), checksumBuilder.toString()));
                    } catch (Throwable t) {
                        LOGGER.error("Caught exception:", t);
                    }
                } else {
                    checksumBuilder.append(character);
                }
            }

        }
        return contents;
    }


}
