package nl.hoepsch.pm;

import nl.hoepsch.pm.config.PowerMeterConfigurationProperties;
import nl.hoepsch.pm.model.LogLine;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

/**
 * A {@link DataPublisher} for the gas meter's readout.
 * <p>
 * It will publish the total number of M3 used.
 */
@Component
public class GasMeterDataPublisher extends AbstractDataPublisher implements DataPublisher {

    /**
     * The constructor.
     */
    public GasMeterDataPublisher(final Producer<Long, String> kafkaProducer,
            final PowerMeterConfigurationProperties configurationProperties) {
        super(kafkaProducer, configurationProperties);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD.LawOfDemeter")
    @Override
    protected String getTopic() {
        return getConfigurationProperties().getGasMeterTopic();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPayload(final LogLine logLine) {
        return format("%s\t%s\t%s\t%s\t%s\t%s", logLine.getTimestamp(), logLine.getMeterId(), logLine.getMeterName(), logLine.getLogDate(),
                logLine.getGasTimestamp(), logLine.getGas());
    }

}
