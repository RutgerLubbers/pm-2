package nl.hoepsch.pm.kafka.receiver;

import nl.hoepsch.pm.dsmr.dto.DSMR5DatagramDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static nl.hoepsch.pm.config.KafkaTopics.DATAGRAM_TOPIC;

/**
 * Listener for datagrams.
 */
@Service
public class DatagramReceiver {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DatagramReceiver.class);

    /**
     * Listen method for DSMR datagrams.
     *
     * @param dto The payload.
     */
    @KafkaListener(topics = DATAGRAM_TOPIC)
    public void receiveDatagram(final DSMR5DatagramDto dto) {
        LOGGER.debug("Got datagram ..");
    }
}
