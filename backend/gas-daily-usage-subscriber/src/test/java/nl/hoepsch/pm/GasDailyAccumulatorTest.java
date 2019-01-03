package nl.hoepsch.pm;

import nl.hoepsch.pm.config.InitialMeterReadoutsConfigurationProperties;
import nl.hoepsch.pm.config.PowerMeterConfigurationProperties;
import nl.hoepsch.pm.model.AccumulatedValue;
import nl.hoepsch.pm.model.GasMeterReadout;
import nl.hoepsch.pm.model.GasMeterReadoutAssembler;
import nl.hoepsch.pm.model.InitialMeterReadouts;
import nl.hoepsch.pm.model.InitialMeterReadoutsAssembler;
import nl.hoepsch.pm.model.Meter;
import nl.hoepsch.pm.model.MeterAssembler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GasDailyAccumulatorTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String METER_ID = "meter_id";
    private static final String METER_NAME = "meter_name";

    private GasDailyUsageAccumulator accumulator;

    @Mock
    private InitialMeterReadoutsAssembler initialMeterReadoutsAssembler;

    @Before
    public void initializeAccumulator() throws Exception {
        final MeterAssembler meterAssembler = new MeterAssembler();
        final GasMeterReadoutAssembler gasMeterReadoutAssembler = new GasMeterReadoutAssembler(meterAssembler);
        final PowerMeterConfigurationProperties configurationProperties = new PowerMeterConfigurationProperties();

        final InitialMeterReadoutsConfigurationProperties initialMeterReadoutProperties = new InitialMeterReadoutsConfigurationProperties();
        initialMeterReadoutProperties.setMeterId(METER_ID);
        initialMeterReadoutProperties.setMeterName(METER_NAME);
        configurationProperties.getInitialMeterReadouts().add(initialMeterReadoutProperties);

        accumulator = new GasDailyUsageAccumulator(gasMeterReadoutAssembler, initialMeterReadoutsAssembler, configurationProperties);



        final Meter meter = new Meter();
        meter.setId(METER_ID);
        meter.setName(METER_NAME);

        final GasMeterReadout gasMeterReadout = new GasMeterReadout();
        gasMeterReadout.setMeter(meter);
        gasMeterReadout.setValue(1000L);
        gasMeterReadout.setReadoutTimestamp(LocalDateTime.parse("2018-01-01 23:00:00", FORMATTER));
        gasMeterReadout.setTimestamp(LocalDateTime.parse("2018-01-01 23:00:00", FORMATTER));

        final InitialMeterReadouts initialMeterReadout = new InitialMeterReadouts();
        initialMeterReadout.setGasMeterReadout(gasMeterReadout);
        when(initialMeterReadoutsAssembler.convert(initialMeterReadoutProperties)).thenReturn(initialMeterReadout);

        accumulator.afterPropertiesSet();
    }

    @Test
    public void testFirstValue() {
        accumulator.consume(createRecord(0L, "2018-01-01T23:50:00", "2018-01-01", "230000", 1000L), false);
        final AccumulatedValue<Long> value = accumulator.getAccumulatedValue();
        assertThat(value.getValue(), is(equalTo(0L)));

        accumulator.consume(createRecord(0L, "2018-01-02T00:50:00", "2018-01-02", "000000", 1200L), false);
        assertThat(value.getValue(), is(equalTo(200L)));
        accumulator.consume(createRecord(0L, "2018-01-02T23:50:00", "2018-01-02", "230000", 2200L), false);

        accumulator.consume(createRecord(0L, "2018-01-03T00:50:00", "2018-01-03", "000000", 2300L), false);
    }

    private ConsumerRecord<Long, String> createRecord(final Long id, final String timestamp, final String date, final String time,
            final Long readout) {
        //        private static final int TIMESTAMP = 0;
        //        private static final int METER_ID = 1;
        //        private static final int METER_NAME = 2;
        //
        //        /**
        //         * gas_meter_value
        //         */
        //        private static final int GAS_READ_DATE = 3;
        //        private static final int GAS_READ_TIME = 4;
        //        private static final int GAS_METER_READOUT = 5;

        return new ConsumerRecord<>("topic", 0, 0L, id, format("%s\t%s\t%s\t%s\t%s\t%s", timestamp, METER_ID, METER_NAME, date, time, readout));
    }
}
