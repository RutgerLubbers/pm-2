package nl.hoepsch.pm.electricity.meter;


import nl.hoepsch.pm.config.SharedMapperConfig;
import nl.hoepsch.pm.electricity.meter.dto.ElectricityMeterReadoutDto;
import nl.hoepsch.pm.electricity.meter.model.ElectricityMeterReadout;
import nl.hoepsch.pm.kafka.AvroDateTimeMapper;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Maps between ElectricityActualReadout model and DTO.
 */
@Mapper(config = SharedMapperConfig.class, unmappedTargetPolicy = IGNORE, uses = AvroDateTimeMapper.class)
public interface ElectricityMeterReadoutMapper {

    /**
     * Maps from model to DTO.
     *
     * @param input The {@link ElectricityMeterReadout} to map.
     * @return An instance of {@link ElectricityMeterReadoutDto}.
     */
    ElectricityMeterReadoutDto map(ElectricityMeterReadout input);

    /**
     * Maps from DTO to model.
     *
     * @param input The {@link ElectricityMeterReadoutDto} to map.
     * @return An instance of {@link ElectricityMeterReadout}.
     */
    ElectricityMeterReadout map(ElectricityMeterReadoutDto input);
}
