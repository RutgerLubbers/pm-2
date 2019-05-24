package nl.hoepsch.pm;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import nl.hoepsch.pm.dsmr.FileDatagramReader;
import nl.hoepsch.pm.kafka.KafkaDatagramAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * The command line runner for the p1 reader.
 */
@Component
public class P1ReaderHackPublisherCommandLineRunner implements CommandLineRunner {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(P1ReaderHackPublisherCommandLineRunner.class);

    /**
     * The file reader.
     */
    private final FileDatagramReader fileDatagramReader;

    /**
     * The datagram acceptor.
     */
    private final DatagramAcceptor datagramAcceptor;

    /**
     * The constructor.
     *
     * @param fileDatagramReader The file reader.
     * @param datagramAcceptor   The datagram acceptor.
     */
    @Autowired
    public P1ReaderHackPublisherCommandLineRunner(final FileDatagramReader fileDatagramReader,
        final KafkaDatagramAcceptor datagramAcceptor) {
        this.fileDatagramReader = fileDatagramReader;
        this.datagramAcceptor = datagramAcceptor;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressFBWarnings("DMI_HARDCODED_ABSOLUTE_FILENAME")
    @Override
    public void run(final String... args) {
        LOGGER.info("Reading files.");

        fileDatagramReader.readAll("/Users/tapir/power-meter-2/p1-reader-hack/zip", datagramAcceptor);
    }
}
