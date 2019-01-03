package nl.hoepsch.pm;

import nl.hoepsch.pm.config.PowerMeterConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The main application.
 */
@SuppressWarnings("PMD.ShortClassName")
@SpringBootApplication
@EnableScheduling
public class GasYtdUsageSubscriber {

    /**
     * The configuration properties for this application.
     */
    @Autowired
    private PowerMeterConfigurationProperties powerMeterConfigurationProperties;

    /**
     * The Record Consumer.
     */
    @Autowired
    private GasYtdUsageAccumulator gasYtdUsageAccumulator;

    /**
     * The Kafka Subscription.
     */
    @Bean
    public KafkaSubscription kafkaSubscription() {
        return new KafkaSubscription(powerMeterConfigurationProperties, gasYtdUsageAccumulator);
    }

    /**
     * The main method to run.
     */
    public static void main(final String[] args) throws InterruptedException {
        SpringApplication.run(GasYtdUsageSubscriber.class, args);
    }
}
