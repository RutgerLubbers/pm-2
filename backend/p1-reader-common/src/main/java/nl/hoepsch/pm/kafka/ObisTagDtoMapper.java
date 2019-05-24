package nl.hoepsch.pm.kafka;

import nl.hoepsch.pm.config.SharedMapperConfig;
import nl.hoepsch.pm.dsmr.dto.ObisTagDto;
import nl.hoepsch.pm.dsmr.model.ObisTag;
import org.mapstruct.Mapper;

/**
 * Mapper to create the DTO from the model.
 */
@Mapper(config = SharedMapperConfig.class)
public interface ObisTagDtoMapper {

    /**
     * Maps the input {@link ObisTag} onto the {@link ObisTagDto}.
     *
     * @param model The input.
     * @return The mapped output.
     */
    ObisTagDto map(ObisTag model);

    /**
     * Maps the input {@link ObisTagDto} onto the {@link ObisTag}.
     *
     * @param model The input.
     * @return The mapped output.
     */
    ObisTag map(ObisTagDto model);
}
