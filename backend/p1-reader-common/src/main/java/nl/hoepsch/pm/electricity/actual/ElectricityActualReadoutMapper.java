package nl.hoepsch.pm.electricity.actual;

import nl.hoepsch.pm.config.SharedMapperConfig;
import nl.hoepsch.pm.electricity.actual.dto.ElectricityActualReadoutDto;
import nl.hoepsch.pm.electricity.actual.model.ElectricityActualReadout;
import nl.hoepsch.pm.kafka.AvroDateTimeMapper;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Maps between ElectricityActualReadout model and DTO.
 */
@Mapper(config = SharedMapperConfig.class, unmappedTargetPolicy = IGNORE, uses = AvroDateTimeMapper.class)
public interface ElectricityActualReadoutMapper {

    /**
     * Maps from model to DTO.
     *
     * @param input The {@link ElectricityActualReadout} to map.
     * @return An instance of {@link ElectricityActualReadoutDto}.
     */
    ElectricityActualReadoutDto map(ElectricityActualReadout input);

    /**
     * Maps from DTO to model.
     *
     * @param input The {@link ElectricityActualReadoutDto} to map.
     * @return An instance of {@link ElectricityActualReadout}.
     */
    ElectricityActualReadout map(ElectricityActualReadoutDto input);

}
