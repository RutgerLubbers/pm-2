package nl.hoepsch.pm;

import nl.hoepsch.pm.model.LogLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * The component responsible to read the data and send the data to the publishers.
 */
@Component
public class DataSender {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSender.class);

    /**
     * The data provider.
     */
    private final DataProvider dataProvider;

    /**
     * The list of Kafka publishers that publish the data to various topics.
     */
    private final List<DataPublisher> publishers;

    /**
     * The number of items to publish.
     */
    @Value("${powerMeter.stub.publishAmount}")
    private Integer publishAmount;

    private int lineNr = 0;

    /**
     * The constructor.
     */
    @Autowired
    public DataSender(final DataProvider dataProvider, final List<DataPublisher> publishers) {
        this.dataProvider = requireNonNull(dataProvider);
        this.publishers = requireNonNull(publishers);
    }

    /**
     * Publish the next value from {@code dataProvider} to the {@code publishers}.
     */
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    @Scheduled(initialDelay = 1000, fixedRateString = "#{${powerMeter.stub.publishSchedule:5} * 1000}")
    public void publishValues() throws Exception {
        for (int i = 0; i < publishAmount; i++) {

            final LogLine logLine = dataProvider.get();
            if (logLine != null) {
                if (lineNr == 0) {
                    LOGGER.debug("Sending log line at '{}'.", logLine.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME));
                }
                lineNr = (lineNr + 1) % 10;
                for (final DataPublisher publisher : publishers) {
                    publisher.publish(logLine);
                }
            } else {
                LOGGER.debug("Log line was null, skipping loop.");
                break;
            }
        }
    }

}
