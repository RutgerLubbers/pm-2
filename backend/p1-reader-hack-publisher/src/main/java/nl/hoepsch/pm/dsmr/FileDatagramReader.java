package nl.hoepsch.pm.dsmr;

import nl.hoepsch.pm.DatagramAcceptor;
import nl.hoepsch.pm.dsmr.model.DSMR5Datagram;
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

@Component
public class FileDatagramReader {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDatagramReader.class);

    public void readAll(final String directory, final DatagramAcceptor acceptor) {
        Path zipDir = Paths.get(directory);
        //        final Path zipFile = zipDir.resolve("p1-20190108.zip");
        //        readZipFile(zipFile);
        try {
            Files.list(zipDir).sorted().forEach(zipFile -> readZipFile(zipFile, acceptor));
        } catch (IOException ex) {
        }

    }

    public void readZipFile(final Path zipFile, final DatagramAcceptor acceptor) {
        try {
            final FileSystem fs = FileSystems.newFileSystem(zipFile, FileDatagramReader.class.getClassLoader());
            for (Path root : fs.getRootDirectories()) {
                Files.walk(root).sorted().forEach(
                    zipFileEntry -> {
                        if (Files.isRegularFile(zipFileEntry)) {
                            try {
                                LOGGER.debug("Reading file '{}{}'.", zipFile.getFileName(), zipFileEntry);
                                final List<DSMR5Datagram> datagrams = readFile(zipFileEntry);
                                datagrams.forEach(acceptor::accept);
                                System.exit(1);
                            } catch (IOException e) {
                            }
                        }
                    }
                );
            }
        } catch (IOException ex) {

        }
    }

    private static List<DSMR5Datagram> readFile(final Path gzipFile) throws IOException {
        try (
            final InputStream in = Files.newInputStream(gzipFile);
            GZIPInputStream gis = new GZIPInputStream(in);
            BufferedReader reader = new BufferedReader(new InputStreamReader(gis))) {

            return readDatagrams(reader);
        }
    }

    private static List<DSMR5Datagram> readDatagrams(final BufferedReader reader) throws IOException {
        final List<DSMR5Datagram> contents = new ArrayList<>();

        boolean lastLineOfDatagram = false;
        StringBuilder datagramBuilder = new StringBuilder();
        StringBuilder checksumBuilder = new StringBuilder();
        int i;
        while ((i = reader.read()) != -1) {
            char c = (char) i;
            if (c == '/') {
                // New datagram
                lastLineOfDatagram = false;
                datagramBuilder = new StringBuilder();
                checksumBuilder = new StringBuilder();
            }

            if (!lastLineOfDatagram) {
                if (c == '\n') {
                    datagramBuilder.append('\r');
                }
                if (c == '!') {
                    // Last line of datagram
                    lastLineOfDatagram = true;
                }
                datagramBuilder.append(c);
            } else {
                if (c == '\n') {
                    lastLineOfDatagram = false;
                    try {
                        contents.add(new DSMR5Datagram(datagramBuilder.toString(), checksumBuilder.toString()));
                    } catch (Throwable t) {
                        LOGGER.error("Caught exception:", t);
                    }
                } else {
                    checksumBuilder.append(c);
                }
            }

        }
        return contents;
    }


}
