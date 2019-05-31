package nl.hoepsch.pm.electricity.meter;


import nl.hoepsch.pm.config.SharedMapperConfig;
import nl.hoepsch.pm.electricity.meter.dto.ElectricityMeterPeriodReadoutDto;
import nl.hoepsch.pm.electricity.meter.model.ElectricityMeterPeriodReadout;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Maps between ElectricityActualReadout model and DTO.
 */
@Mapper(config = SharedMapperConfig.class, unmappedTargetPolicy = IGNORE, uses = ElectricityMeterReadoutMapper.class)
public interface ElectricityMeterPeriodReadoutMapper {

    /**
     * Maps from model to DTO.
     *
     * @param input The {@link ElectricityMeterPeriodReadout} to map.
     * @return An instance of {@link ElectricityMeterPeriodReadoutDto}.
     */
    ElectricityMeterPeriodReadoutDto map(ElectricityMeterPeriodReadout input);

    /**
     * Maps from DTO to model.
     *
     * @param input The {@link ElectricityMeterPeriodReadoutDto} to map.
     * @return An instance of {@link ElectricityMeterPeriodReadout}.
     */
    ElectricityMeterPeriodReadout map(ElectricityMeterPeriodReadoutDto input);
}
