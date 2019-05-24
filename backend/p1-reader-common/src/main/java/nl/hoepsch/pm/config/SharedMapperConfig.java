package nl.hoepsch.pm.config;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;

/**
 * Shared mapStruct mapper configuration.
 */
@MapperConfig(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SharedMapperConfig {

}
