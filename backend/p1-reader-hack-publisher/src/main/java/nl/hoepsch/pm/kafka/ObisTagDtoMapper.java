package nl.hoepsch.pm.kafka;

import nl.hoepsch.pm.config.SharedMapperConfig;
import nl.hoepsch.pm.dsmr.dto.DSMR5DatagramDto;
import nl.hoepsch.pm.dsmr.dto.ObisTagDto;
import nl.hoepsch.pm.dsmr.model.DSMR5Datagram;
import nl.hoepsch.pm.dsmr.model.ObisTag;
import org.mapstruct.Mapper;

/**
 * Mapper to create datagram DTO from the model.
 */
@Mapper(config = SharedMapperConfig.class)
public interface ObisTagDtoMapper {

    ObisTagDto toDto(ObisTag model);
}
