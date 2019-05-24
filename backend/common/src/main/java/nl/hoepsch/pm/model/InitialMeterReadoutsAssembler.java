package nl.hoepsch.pm.model;

import nl.hoepsch.pm.config.ElectricityRateMeterConfigurationProperties;
import nl.hoepsch.pm.config.InitialMeterReadoutsConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Assembles an {@link InitialMeterReadouts} from the configuration properties.
 */
@Component
public class InitialMeterReadoutsAssembler {

    /**
     * Converts the {@code configuration} into an {@link InitialMeterReadouts}.
     */
    public InitialMeterReadouts convert(final InitialMeterReadoutsConfigurationProperties configuration) {
        final InitialMeterReadouts initialReadouts = new InitialMeterReadouts();

        final Meter meter = createMeter(configuration);
        initialReadouts.setGasMeterReadout(createGasMeterReadout(meter, configuration));
        initialReadouts.setElectricityMeterReadout(createElectricityMeterReadout(meter, configuration));
        return initialReadouts;
    }

    private Meter createMeter(final InitialMeterReadoutsConfigurationProperties configuration) {
        final Meter meter = new Meter();
        meter.setId(configuration.getMeterId());
        meter.setName(configuration.getMeterName());
        return meter;
    }

    private GasMeterReadout createGasMeterReadout(final Meter meter, final InitialMeterReadoutsConfigurationProperties configuration) {
        final GasMeterReadout readout = new GasMeterReadout();
        readout.setMeter(meter);
        readout.setTimestamp(configuration.getParsedTimestamp());
        readout.setReadoutTimestamp(configuration.getParsedTimestamp());
        readout.setValue(configuration.getGasMeter());
        return readout;
    }

    private ElectricityMeterReadout createElectricityMeterReadout(final Meter meter,
            final InitialMeterReadoutsConfigurationProperties configuration) {
        final ElectricityMeterReadout readout = new ElectricityMeterReadout();
        readout.setMeter(meter);
        readout.setDayRateValue(createRate(configuration.getElectricityDayRateMeter()));
        readout.setNightRateValue(createRate(configuration.getElectricityNightRateMeter()));
        return readout;
    }

    private ElectricityReadout createRate(final ElectricityRateMeterConfigurationProperties configuration) {
        final ElectricityReadout rate = new ElectricityReadout();
        rate.setSupply(configuration.getSupply());
        rate.setUsage(configuration.getUsage());
        return rate;
    }
}
