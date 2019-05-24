package nl.hoepsch.pm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Configures the Kafka stream(s).
 */
@Configuration
@EnableKafka
public class KafkaStreamsConfig {

//    /**
//     * The servers from the cluster for the Kafka communication.
//     */
//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrapServers;
//
//    /**
//     * The url for the schema registry.
//     */
//    @Value("${spring.kafka.schema.registry.url}")
//    private String schemaRegistryUrl;
//
//    /**
//     * The url for the schema registry.
//     */
//    @Value("${spring.kafka.schema.registry.initial-capacity}")
//    private int schemaRegistryInitialCapacity;
//
//    /**
//     * Kafka streams config.
//     *
//     * @param p1ReaderTopology The topology is an acyclic graph of sources, processors, and sinks.
//     * @return a started version of Kafka streams.
//     */
//    @Bean
//    public KafkaStreams shoppingCartStreams(final Topology p1ReaderTopology) {
//        final Properties props = new Properties();
//        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "p1-reader-stream");
//        props.put(SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
//        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");
//        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
//        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
//        final KafkaStreams streams = new KafkaStreams(p1ReaderTopology, props);
//        streams.cleanUp();
//        streams.start();
//        return streams;
//    }
//
//    @Bean
//    public StreamsBuilder streamsBuilder() {
//        return new StreamsBuilder();
//    }
//    /**
//     * Create a table of shopping carts which we can query.
//     *
//     * @return the topology that can be used to query shopping carts.
//     */
//    @Bean
//    public void p1ReaderTopology(final StreamsBuilder streamsBuilder) {
//        streamsBuilder.stream(KafkaTopics.DATAGRAM_TOPIC.getName()).process();
//    }

}
