package nl.hoepsch.pm.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import nl.hoepsch.pm.dsmr.dto.DSMR5DatagramDto;
import nl.hoepsch.pm.electricity.actual.dto.ElectricityActualReadoutDto;
import nl.hoepsch.pm.electricity.meter.kafka.ElectricityMeterReadoutToDayTransformer;
import nl.hoepsch.pm.electricity.meter.dto.ElectricityMeterPeriodReadoutDto;
import nl.hoepsch.pm.electricity.meter.dto.ElectricityMeterReadoutDto;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.Properties;

import static nl.hoepsch.pm.config.KafkaTopics.DATAGRAM_TOPIC;
import static nl.hoepsch.pm.config.KafkaTopics.ELECTRICITY_ACTUAL_TOPIC;
import static nl.hoepsch.pm.config.KafkaTopics.ELECTRICITY_METER_PER_DAY_VIEW;
import static nl.hoepsch.pm.config.KafkaTopics.ELECTRICITY_METER_TOPIC;

/**
 * Configures the Kafka stream(s).
 */
@Configuration
@EnableKafka
public class KafkaStreamsConfig {

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
     * The url for the schema registry.
     */
    @Value("${spring.kafka.schema.registry.initial-capacity}")
    private int schemaRegistryInitialCapacity;

    /**
     * The Kafka Streams application id.
     */
    @Value("${spring.kafka.streams.application-id}")
    private String applicationId;

    /**
     * The Kafka Streams client id.
     */
    @Value("${spring.kafka.streams.client-id}")
    private String clientId;

    /**
     * The directory where Kafka Streams will keep it's state.
     */
    @Value("${spring.kafka.streams.state-dir}")
    private String stateDir;

    /**
     * Kafka streams config.
     *
     * @param p1ReaderTopology The topology is an acyclic graph of sources, processors, and sinks.
     * @return a started version of Kafka streams.
     */
    @Bean
    public KafkaStreams powerMeterStreams(final Topology p1ReaderTopology) {
        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.CLIENT_ID_CONFIG, clientId);
        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.STATE_DIR_CONFIG, stateDir);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);

        final KafkaStreams streams = new KafkaStreams(p1ReaderTopology, props);
        streams.cleanUp();
        streams.start();
        return streams;
    }

    /**
     * Create a table of shopping carts which we can query.
     *
     * @return the topology that can be used to query shopping carts.
     */
    @Bean
    public Topology p1ReaderTopology(
        final ValueMapper<DSMR5DatagramDto, ElectricityActualReadoutDto> toElectricityActualReadoutMapper,
        final ValueMapper<DSMR5DatagramDto, ElectricityMeterReadoutDto> toElectricityMeterReadoutMapper,
        final ElectricityMeterReadoutToDayTransformer toDayTransformer) {
        return createTopology(new StreamsBuilder(), toElectricityActualReadoutMapper, toElectricityMeterReadoutMapper, toDayTransformer);
    }


    @SuppressWarnings("PMD.ExcessiveMethodLength")
    private Topology createTopology(final StreamsBuilder streamsBuilder,
        final ValueMapper<DSMR5DatagramDto, ElectricityActualReadoutDto> toElectricityActualReadoutMapper,
        final ValueMapper<DSMR5DatagramDto, ElectricityMeterReadoutDto> toElectricityMeterReadoutMapper,
        final ElectricityMeterReadoutToDayTransformer toDayTransformer) {

        /*
         * Read the datagram topic and create a stream of it.
         */
        final KStream<String, DSMR5DatagramDto> datagramStream = streamsBuilder.stream(DATAGRAM_TOPIC);

        /*
         * Create an electricity actual value stream from the datagram stream.
         */
        final KStream<String, ElectricityActualReadoutDto> electricityActualStream =
            datagramStream.mapValues(toElectricityActualReadoutMapper);
        /*
         * Publish the electricity actual stream to the electricity actual topic.
         */
        electricityActualStream.to(ELECTRICITY_ACTUAL_TOPIC);

        /*
         * Create an electricity meter value stream from the datagram stream.
         */
        final KStream<String, ElectricityMeterReadoutDto> electricityMeterStream =
            datagramStream.mapValues(toElectricityMeterReadoutMapper);
        /*
         * Publish the electricity meter stream to the electricity meter topic.
         */
        electricityMeterStream.to(ELECTRICITY_METER_TOPIC);

        /*
         * Create an electricity meter stream with the day as the key.
         */
        final KStream<String, ElectricityMeterReadoutDto> electricityMeterDayStream =
            electricityMeterStream.transform(() -> toDayTransformer);

        /*
         * Aggregate the electricity meter stream
         */
        electricityMeterDayStream.groupByKey().aggregate(
            ElectricityMeterPeriodReadoutDto::new,
            (key, value, aggregate) -> {
                final ElectricityMeterReadoutDto start = aggregate.getStart();
                if (start == null || start.getTimestamp() > value.getTimestamp()) {
                    aggregate.setStart(value);
                }
                final ElectricityMeterReadoutDto end = aggregate.getEnd();
                if (end == null || end.getTimestamp() < value.getTimestamp()) {
                    aggregate.setEnd(value);
                }

                return aggregate;
            },
            Materialized.as(ELECTRICITY_METER_PER_DAY_VIEW)
        );

        return streamsBuilder.build();
    }


}
