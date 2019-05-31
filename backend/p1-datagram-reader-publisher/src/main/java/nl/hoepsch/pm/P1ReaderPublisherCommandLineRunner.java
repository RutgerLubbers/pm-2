package nl.hoepsch.pm;

import nl.hoepsch.pm.dsmr.DatagramAcceptor;
import nl.hoepsch.pm.dsmr.FileDatagramReader;
import nl.hoepsch.pm.dsmr.KafkaDatagramAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The command line runner for the p1 reader.
 */
@SuppressWarnings({"PMD.SingularField", "PMD.UnusedPrivateField"})
@Component
public class P1ReaderPublisherCommandLineRunner implements CommandLineRunner {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(P1ReaderPublisherCommandLineRunner.class);

    /**
     * The file reader.
     */
    private final FileDatagramReader fileDatagramReader;

    /**
     * The datagram acceptor.
     */
    private final DatagramAcceptor datagramAcceptor;

    /**
     * Path for stored, older, P1 datagrams.
     */
    @Value("${datagram-store.historical}")
    private String datagramStore;

    /**
     * The constructor.
     *
     * @param fileDatagramReader The file reader.
     * @param datagramAcceptor   The datagram acceptor.
     */
    @Autowired
    public P1ReaderPublisherCommandLineRunner(final FileDatagramReader fileDatagramReader,
        final KafkaDatagramAcceptor datagramAcceptor) {
        this.fileDatagramReader = fileDatagramReader;
        this.datagramAcceptor = datagramAcceptor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(final String... args) {
        LOGGER.info("Started P1 Reader Publisher App.");

        if (pathExists(datagramStore)) {
            LOGGER.info("Reading stored datagrams from '{}'.", datagramStore);
            fileDatagramReader.readAll(datagramStore, datagramAcceptor);
        }

    }

    private boolean pathExists(final String path) {
        return Files.exists(Paths.get(path));
    }

}
