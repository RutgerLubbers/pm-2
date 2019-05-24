package nl.hoepsch.pm;

import nl.hoepsch.pm.config.KafkaConfigurationProperties;
import nl.hoepsch.pm.config.PowerMeterConfigurationProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Properties;

/**
 * The spring boot application for sending (stubbed, pre recorded) P1 meter readings.
 */
@SpringBootApplication
@EnableScheduling
public class P1ReaderStub {

    /**
     * The power meter configuration properties.
     */
    @Autowired
    private PowerMeterConfigurationProperties powerMeterConfigurationProperties;

    /**
     * Create a KafkaProducer.
     */
    @Bean
    public KafkaProducer<Long, String> createGasProducer() {
        final KafkaConfigurationProperties kafka = powerMeterConfigurationProperties.getKafka();

        return createProducer(kafka);
    }

    private KafkaProducer<Long, String> createProducer(final KafkaConfigurationProperties kafka) {
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new KafkaProducer<>(props);
    }

    /**
     * Create a new Spring Boot application for this class.
     */
    public static void main(final String[] args) {
        SpringApplication.run(P1ReaderStub.class, args);
    }
}
