package nl.hoepsch.pm.config;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import nl.hoepsch.pm.dsmr.dto.DSMR5DatagramDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Configures the Kafka template(s).
 */
@Configuration
@EnableKafka
public class KafkaProducerConfig {

    /**
     * The servers from the cluster for the Kafka communication.
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * The url for the schema registry.
     */
    @Value("${spring.kafka.schema.registry.url}")
    private String schemaRegistryUrl;

    /**
     * Configuration for the producer config.
     *
     * @return the configuration.
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        final Map<String, Object> props = new HashMap<>();
        // list of host:port pairs used for establishing the initial connections to the Kafka cluster
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        return props;
    }

    /**
     * A shopping cart producer factory using the shopping cart value serializer.
     *
     * @return a new producer factory.
     */
    @Bean
    public ProducerFactory<String, DSMR5DatagramDto> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean(name = "datagramKafkaTemplate")
    public KafkaTemplate<String, DSMR5DatagramDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    //    /**
    //     * Setup a reply kafka template.
    //     *
    //     * @param producerFactory the producer factory,
    //     * @param container       the container.
    //     * @return the reply kafka template.
    //     */
    //    @Bean
    //    public ReplyingKafkaTemplate<String, ShoppingCartDto, OrderReferenceDto> replyKafkaTemplate(
    //            final ProducerFactory<String, ShoppingCartDto> producerFactory,
    //            final KafkaMessageListenerContainer<String, OrderReferenceDto> container) {
    //        return new ReplyingKafkaTemplate<>(producerFactory, container);
    //    }
    //
    //    /**
    //     * The reply container.
    //     *
    //     * @param consumerFactory the consumer factory.
    //     * @return a reply container.
    //     */
    //    @Bean
    //    public KafkaMessageListenerContainer<String, OrderReferenceDto> replyContainer(
    //            final ConsumerFactory<String, OrderReferenceDto> consumerFactory) {
    //        final ContainerProperties containerProperties = new ContainerProperties(SHOPPING_CART_EXTERNAL_VALIDATION);
    //        return new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    //    }

}
