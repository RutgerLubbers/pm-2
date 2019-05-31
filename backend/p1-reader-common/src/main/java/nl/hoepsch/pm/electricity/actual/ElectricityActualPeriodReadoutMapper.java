package nl.hoepsch.pm.electricity.actual;


import nl.hoepsch.pm.config.SharedMapperConfig;
import nl.hoepsch.pm.electricity.actual.dto.ElectricityActualPeriodReadoutDto;
import nl.hoepsch.pm.electricity.actual.model.ElectricityActualPeriodReadout;
import nl.hoepsch.pm.electricity.meter.dto.ElectricityMeterPeriodReadoutDto;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Maps between ElectricityActualReadout model and DTO.
 */
@Mapper(config = SharedMapperConfig.class, unmappedTargetPolicy = IGNORE, uses = ElectricityActualReadoutMapper.class)
public interface ElectricityActualPeriodReadoutMapper {

    /**
     * Maps from model to DTO.
     *
     * @param input The {@link ElectricityActualPeriodReadout} to map.
     * @return An instance of {@link ElectricityMeterPeriodReadoutDto}.
     */
    ElectricityActualPeriodReadoutDto map(ElectricityActualPeriodReadout input);

    /**
     * Maps from DTO to model.
     *
     * @param input The {@link ElectricityMeterPeriodReadoutDto} to map.
     * @return An instance of {@link ElectricityActualPeriodReadoutDto}.
     */
    ElectricityActualPeriodReadout map(ElectricityActualPeriodReadoutDto input);
}
