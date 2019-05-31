package nl.hoepsch.pm;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import nl.hoepsch.pm.dsmr.DatagramAcceptor;
import nl.hoepsch.pm.dsmr.FileDatagramReader;
import nl.hoepsch.pm.dsmr.KafkaDatagramAcceptor;
import nl.hoepsch.pm.dsmr.SerialPortDatagramReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
     * The serial port smart meter reader.
     */
    private final SerialPortDatagramReader serialPortDatagramReader;

    @Value("${datagram-store}")
    private String datagramStore;

    /**
     * The constructor.
     *
     * @param fileDatagramReader       The file reader.
     * @param datagramAcceptor         The datagram acceptor.
     * @param serialPortDatagramReader The serial port smart meter reader.
     */
    @Autowired
    public P1ReaderPublisherCommandLineRunner(final FileDatagramReader fileDatagramReader,
        final KafkaDatagramAcceptor datagramAcceptor, final SerialPortDatagramReader serialPortDatagramReader) {
        this.fileDatagramReader = fileDatagramReader;
        this.datagramAcceptor = datagramAcceptor;
        this.serialPortDatagramReader = serialPortDatagramReader;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressFBWarnings("DMI_HARDCODED_ABSOLUTE_FILENAME")
    @Override
    public void run(final String... args) {
        LOGGER.info("Started P1 Reader Publisher App.");

        try {
            serialPortDatagramReader.consume();
        } catch (InterruptedException e) {
            LOGGER.info("Got interrupted.", e);
        }

        if (pathExists(datagramStore)) {
            LOGGER.info("Reading stored datagrams from '{}'.", datagramStore);
            // fileDatagramReader.readAll("/Users/tapir/power-meter-2/p1-reader-hack/zip", datagramAcceptor);
            // fileDatagramReader.readAll("/Users/tapir/power-meter-2/data/one-zip", datagramAcceptor);
        }

    }

    private boolean pathExists(final String path) {
        return Paths.get(path).toFile().exists();
    }

}
