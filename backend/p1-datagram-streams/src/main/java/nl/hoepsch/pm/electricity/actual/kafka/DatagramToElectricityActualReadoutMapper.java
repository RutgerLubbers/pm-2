package nl.hoepsch.pm.electricity.actual.kafka;

import nl.hoepsch.pm.dsmr.converter.TagValueExtractor;
import nl.hoepsch.pm.dsmr.dto.DSMR5DatagramDto;
import nl.hoepsch.pm.dsmr.model.DSMR5Datagram;
import nl.hoepsch.pm.electricity.actual.ElectricityActualReadoutMapper;
import nl.hoepsch.pm.electricity.actual.dto.ElectricityActualReadoutDto;
import nl.hoepsch.pm.electricity.actual.model.ElectricityActualReadout;
import nl.hoepsch.pm.kafka.DsmDatagramMapper;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

import static nl.hoepsch.pm.dsmr.model.ObisTagType.ELECTRICITY_DELIVERED_BY_CLIENT_ACTUAL;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.ELECTRICITY_DELIVERED_TO_CLIENT_ACTUAL;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.TIME_STAMP;

/**
 * Listener for DSMR datagrams to extract the actual meter values and propagate them onto a different topic.
 */
@Service
public class DatagramToElectricityActualReadoutMapper implements ValueMapper<DSMR5DatagramDto, ElectricityActualReadoutDto> {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DatagramToElectricityActualReadoutMapper.class);

    /**
     * The mapper to convert a datagram from dto to model.
     */
    private final DsmDatagramMapper datagramMapper;

    /**
     * The mapper to convert electricity actual readouts to and from model and dto.
     */
    private final ElectricityActualReadoutMapper readoutMapper;

    /**
     * Utility to extract a timestamp from an DSMR tag.
     */
    private final TagValueExtractor<ZonedDateTime> timestampExtractor;

    /**
     * Utility to extract a double value from a DSMR tag.
     */
    private final TagValueExtractor<Double> doubleExtractor;

    /**
     * The constructor.
     *
     * @param datagramMapper     The mapper to convert a datagram from dto to model.
     * @param readoutMapper      The mapper to convert electricity actual readouts to and from model and dto.
     * @param timestampExtractor Utility to extract a timestamp from an DSMR tag.
     * @param doubleExtractor    Utility to extract a double value from a DSMR tag.
     */
    @Autowired
    public DatagramToElectricityActualReadoutMapper(final DsmDatagramMapper datagramMapper,
        final ElectricityActualReadoutMapper readoutMapper,
        final TagValueExtractor<ZonedDateTime> timestampExtractor,
        final TagValueExtractor<Double> doubleExtractor) {
        this.datagramMapper = datagramMapper;
        this.readoutMapper = readoutMapper;
        this.timestampExtractor = timestampExtractor;
        this.doubleExtractor = doubleExtractor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ElectricityActualReadoutDto apply(final DSMR5DatagramDto value) {
        LOGGER.debug("Got datagram, converting it into an electricity actual readout ..");
        final DSMR5Datagram datagram = datagramMapper.map(value);

        final ElectricityActualReadout readout = new ElectricityActualReadout();
        readout.setTimestamp(timestampExtractor.extract(datagram, TIME_STAMP));
        readout.setDeliveredToClientMeter(doubleExtractor.extract(datagram, ELECTRICITY_DELIVERED_TO_CLIENT_ACTUAL));
        readout.setDeliveredByClientMeter(doubleExtractor.extract(datagram, ELECTRICITY_DELIVERED_BY_CLIENT_ACTUAL));

        return readoutMapper.map(readout);
    }
}
