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
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

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
     * The datagram reader.
     */
    private final DatagramReader datagramReader;

    /**
     * The constructor.
     *
     * @param datagramReader The datagram reader.
     */
    @Autowired
    public FileDatagramReader(final DatagramReader datagramReader) {
        this.datagramReader = datagramReader;
    }

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
        LOGGER.info("Done reading '{}'.", directory);
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
                                readFile(zipFileEntry, acceptor);
                                Thread.sleep(50);
                            } catch (IOException | InterruptedException e) {
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

    private void readFile(final Path gzipFile, final DatagramAcceptor acceptor) throws IOException {
        try (
            InputStream inputStream = Files.newInputStream(gzipFile);
            GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(gzipInputStream, Charsets.UTF_8))) {
            datagramReader.consume(reader, acceptor);
        }
    }



}
