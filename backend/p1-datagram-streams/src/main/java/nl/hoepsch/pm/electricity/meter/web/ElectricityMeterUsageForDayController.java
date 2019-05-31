package nl.hoepsch.pm.electricity.meter.web;

import nl.hoepsch.pm.electricity.meter.kafka.ElectricityMeterPerDayView;
import nl.hoepsch.pm.electricity.meter.model.ElectricityMeterPeriodReadout;
import nl.hoepsch.pm.electricity.meter.model.ElectricityMeterReadout;
import nl.hoepsch.pm.electricity.meter.web.resource.ElectricityMeterUsageResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static nl.hoepsch.pm.util.CalculationUtil.subtract;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Rest controller to get electricity meter usage for a day.
 */
@RestController
public class ElectricityMeterUsageForDayController {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ElectricityMeterUsageForDayController.class);

    /**
     * The view as configured in the streams.
     */
    private final ElectricityMeterPerDayView electricityMeterPerDayView;

    /**
     * The constructor.
     *
     * @param electricityMeterPerDayView The view as configured in the streams.
     */
    @Autowired
    public ElectricityMeterUsageForDayController(final ElectricityMeterPerDayView electricityMeterPerDayView) {
        this.electricityMeterPerDayView = electricityMeterPerDayView;
    }

    /**
     * Get the usage for the requested {@code day}.
     *
     * @param day The day to get the usage for.
     * @return The daily usage.
     */
    @GetMapping(path = "/usage/days/{day}", produces = {APPLICATION_JSON_VALUE, APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<ElectricityMeterUsageResource> getUsageForDay(@PathVariable("day") final String day) {
        LOGGER.debug("Getting electricity usage for '{}'.", day);

        return ResponseEntity.ok(createUsage(electricityMeterPerDayView.get(day)));
    }

    private ElectricityMeterUsageResource createUsage(final ElectricityMeterPeriodReadout electricityMeterPeriodReadout) {
        final ElectricityMeterReadout start = electricityMeterPeriodReadout.getStart();
        final ElectricityMeterReadout end = electricityMeterPeriodReadout.getEnd();

        return createUsage(start, end);
    }

    private ElectricityMeterUsageResource createUsage(final ElectricityMeterReadout start, final ElectricityMeterReadout end) {
        final ElectricityMeterUsageResource resource = new ElectricityMeterUsageResource();

        resource.setDeliveredByClientMeter1(subtract(start.getDeliveredByClientMeter1(), end.getDeliveredByClientMeter1()));
        resource.setDeliveredByClientMeter2(subtract(start.getDeliveredByClientMeter2(), end.getDeliveredByClientMeter2()));

        resource.setDeliveredToClientMeter1(subtract(start.getDeliveredToClientMeter1(), end.getDeliveredToClientMeter1()));
        resource.setDeliveredToClientMeter2(subtract(start.getDeliveredToClientMeter2(), end.getDeliveredToClientMeter2()));

        return resource;
    }


}
