package nl.hoepsch.pm.kafka;

import nl.hoepsch.pm.config.SharedMapperConfig;
import nl.hoepsch.pm.dsmr.dto.DSMR5DatagramDto;
import nl.hoepsch.pm.dsmr.model.DSMR5Datagram;
import org.mapstruct.Mapper;

/**
 * Mapper to create the DTO from the model.
 */
@Mapper(config = SharedMapperConfig.class, uses = {ObisTagDtoMapper.class})
public interface DsmDatagramDtoMapper {

    /**
     * Maps the model {@link DSMR5Datagram} onto the {@link DSMR5DatagramDto}.
     *
     * @param model The input.
     * @return The mapped output.
     */
    DSMR5DatagramDto toDto(DSMR5Datagram model);
}
