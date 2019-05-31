package nl.hoepsch.pm.electricity.actual.web;

import nl.hoepsch.pm.electricity.actual.kafka.ElectricityActualPerDayView;
import nl.hoepsch.pm.electricity.meter.web.resource.ElectricityMeterUsageResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Rest controller to get electricity meter usage for a day.
 */
@RestController
public class ElectricityActualPerDayController {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ElectricityActualPerDayController.class);

    /**
     * The view as configured in the streams.
     */
    private final ElectricityActualPerDayView electricityActualPerDayView;

    /**
     * The constructor.
     *
     * @param electricityActualPerDayView The view as configured in the streams.
     */
    @Autowired
    public ElectricityActualPerDayController(final ElectricityActualPerDayView electricityActualPerDayView) {
        this.electricityActualPerDayView = electricityActualPerDayView;
    }

    /**
     * Get the usage for the requested {@code day}.
     *
     * @param day The day to get the usage for.
     * @return The daily usage.
     */
    @GetMapping(path = "/actual/days/{day}", produces = {APPLICATION_JSON_VALUE, APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<ElectricityMeterUsageResource> getUsageForDay(@PathVariable("day") final String day) {
        LOGGER.debug("Getting electricity actual for '{}'.", day);
        electricityActualPerDayView.get(day);
        return ResponseEntity.ok().build();
    }


}
