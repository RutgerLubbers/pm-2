package nl.hoepsch.pm;

import nl.hoepsch.pm.config.PowerMeterConfigurationProperties;
import nl.hoepsch.pm.model.LogLine;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;

/**
 * A {@link DataPublisher} for the current electricity usage.
 *
 * It will publish the current usage / supply of electricity.
 */
@Component
public class ElectricityUsageDataPublisher extends AbstractDataPublisher implements DataPublisher {

    /**
     * The constructor.
     */
    public ElectricityUsageDataPublisher(final Producer<Long, String> kafkaProducer,
            final PowerMeterConfigurationProperties configurationProperties) {
        super(kafkaProducer, configurationProperties);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD.LawOfDemeter")
    @Override
    protected String getTopic() {
        return getConfigurationProperties().getElectricityUsageTopic();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPayload(final LogLine logline) {
        return "EUDP :: data. .. todo";
    }

}
