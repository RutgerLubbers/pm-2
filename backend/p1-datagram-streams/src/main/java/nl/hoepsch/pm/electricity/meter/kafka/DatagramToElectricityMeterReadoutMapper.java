package nl.hoepsch.pm.electricity.meter.kafka;

import nl.hoepsch.pm.dsmr.converter.TagValueExtractor;
import nl.hoepsch.pm.dsmr.dto.DSMR5DatagramDto;
import nl.hoepsch.pm.dsmr.model.DSMR5Datagram;
import nl.hoepsch.pm.electricity.meter.ElectricityMeterReadoutMapper;
import nl.hoepsch.pm.electricity.meter.dto.ElectricityMeterReadoutDto;
import nl.hoepsch.pm.electricity.meter.model.ElectricityMeterReadout;
import nl.hoepsch.pm.kafka.DsmDatagramMapper;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

import static nl.hoepsch.pm.dsmr.model.ObisTagType.ELECTRICITY_DELIVERED_BY_CLIENT_METER_1;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.ELECTRICITY_DELIVERED_BY_CLIENT_METER_2;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.ELECTRICITY_DELIVERED_TO_CLIENT_METER_1;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.ELECTRICITY_DELIVERED_TO_CLIENT_METER_2;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.TIME_STAMP;

/**
 * Listener for DSMR datagrams to extract the actual meter values and propagate them onto a different topic.
 */
@Service
public class DatagramToElectricityMeterReadoutMapper implements ValueMapper<DSMR5DatagramDto, ElectricityMeterReadoutDto> {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DatagramToElectricityMeterReadoutMapper.class);

    /**
     * The mapper to convert a datagram from dto to model.
     */
    private final DsmDatagramMapper datagramMapper;

    /**
     * The mapper to convert electricity actual readouts to and from model and dto.
     */
    private final ElectricityMeterReadoutMapper readoutMapper;

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
     * @param readoutMapper      The mapper to convert electricity meter readouts to and from model and dto.
     * @param timestampExtractor Utility to extract a timestamp from an DSMR tag.
     * @param doubleExtractor    Utility to extract a double value from a DSMR tag.
     */
    @Autowired
    public DatagramToElectricityMeterReadoutMapper(final DsmDatagramMapper datagramMapper,
        final ElectricityMeterReadoutMapper readoutMapper,
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
    public ElectricityMeterReadoutDto apply(final DSMR5DatagramDto value) {
        LOGGER.debug("Got datagram, converting it into an electricity meter readout ..");
        final DSMR5Datagram datagram = datagramMapper.map(value);

        final ElectricityMeterReadout readout = new ElectricityMeterReadout();
        readout.setTimestamp(timestampExtractor.extract(datagram, TIME_STAMP));
        readout.setDeliveredToClientMeter1(doubleExtractor.extract(datagram, ELECTRICITY_DELIVERED_TO_CLIENT_METER_1));
        readout.setDeliveredToClientMeter2(doubleExtractor.extract(datagram, ELECTRICITY_DELIVERED_TO_CLIENT_METER_2));
        readout.setDeliveredByClientMeter1(doubleExtractor.extract(datagram, ELECTRICITY_DELIVERED_BY_CLIENT_METER_1));
        readout.setDeliveredByClientMeter2(doubleExtractor.extract(datagram, ELECTRICITY_DELIVERED_BY_CLIENT_METER_2));

        return readoutMapper.map(readout);
    }
}
