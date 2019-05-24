package nl.hoepsch.pm;

import nl.hoepsch.pm.config.PowerMeterConfigurationProperties;
import nl.hoepsch.pm.model.LogLine;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;

/**
 * A {@link DataPublisher} for the electricity meter's read-out.
 *
 * It will publish the total number of kWh used.
 */
@Component
public class ElectricityMeterDataPublisher extends AbstractDataPublisher implements DataPublisher {

    /**
     * The constructor.
     */
    public ElectricityMeterDataPublisher(final Producer<Long, String> kafkaProducer,
            final PowerMeterConfigurationProperties configurationProperties) {
        super(kafkaProducer, configurationProperties);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD.LawOfDemeter")
    @Override
    protected String getTopic() {
        return getConfigurationProperties().getElectricityMeterTopic();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPayload(final LogLine logline) {
        return "ETDP :: data. .. todo";
    }
}
