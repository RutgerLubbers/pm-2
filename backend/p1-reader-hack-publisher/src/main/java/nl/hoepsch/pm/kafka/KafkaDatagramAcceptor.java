package nl.hoepsch.pm.kafka;

import nl.hoepsch.pm.DatagramAcceptor;
import nl.hoepsch.pm.config.KafkaTopics;
import nl.hoepsch.pm.dsmr.dto.DSMR5DatagramDto;
import nl.hoepsch.pm.dsmr.model.DSMR5Datagram;
import nl.hoepsch.pm.dsmr.model.ObisTagType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka handler for new datagrams.
 */
@Component
public class KafkaDatagramAcceptor implements DatagramAcceptor {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDatagramAcceptor.class);

    /**
     * The template to send a datagram over a Kafka topic.
     */
    private final KafkaTemplate<String, DSMR5DatagramDto> kafkaTemplate;

    /**
     * Datagram mapper.
     */
    private final DsmDatagramDtoMapper dtoMapper;

    /**
     * The constructor.
     *
     * @param kafkaTemplate The template to send a datagram over a Kafka topic.
     * @param dtoMapper     Datagram mapper.
     */
    @Autowired
    public KafkaDatagramAcceptor(@Qualifier("datagramKafkaTemplate") final KafkaTemplate<String, DSMR5DatagramDto> kafkaTemplate,
        final DsmDatagramDtoMapper dtoMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.dtoMapper = dtoMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(final DSMR5Datagram datagram) {
        final String timestamp = datagram.getValue(ObisTagType.TIME_STAMP);
        LOGGER.debug("Publishing datagram '{}'.", timestamp);
        final DSMR5DatagramDto dto = dtoMapper.toDto(datagram);
        kafkaTemplate.send(KafkaTopics.DATAGRAM_TOPIC, dto);
    }
}
