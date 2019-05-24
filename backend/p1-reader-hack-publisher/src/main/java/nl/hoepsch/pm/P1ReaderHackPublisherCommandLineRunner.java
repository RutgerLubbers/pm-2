package nl.hoepsch.pm;

import nl.hoepsch.pm.dsmr.FileDatagramReader;
import nl.hoepsch.pm.kafka.KafkaDatagramAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class P1ReaderHackPublisherCommandLineRunner implements CommandLineRunner {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(P1ReaderHackPublisherCommandLineRunner.class);

    private final FileDatagramReader fileDatagramReader;

    private final KafkaDatagramAcceptor datagramAcceptor;
    /**
     * The constructor.
     *
     * @param fileDatagramReader
     * @param datagramAcceptor
     */
    @Autowired
    public P1ReaderHackPublisherCommandLineRunner(final FileDatagramReader fileDatagramReader,
        final KafkaDatagramAcceptor datagramAcceptor) {
        this.fileDatagramReader = fileDatagramReader;
        this.datagramAcceptor = datagramAcceptor;
    }

    @Override
    public void run(final String... args) throws Exception {
        LOGGER.info("Reading files.");

        fileDatagramReader.readAll("/Users/tapir/power-meter-2/p1-reader-hack/zip", datagramAcceptor);
    }
}
