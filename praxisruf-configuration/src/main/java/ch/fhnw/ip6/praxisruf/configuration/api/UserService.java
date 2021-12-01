package ch.fhnw.ip6.praxisruf.configuration.api;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.UserDto;

import java.util.UUID;

/**
 * This interface specifies interactions with Client entities.
 *
 * @author J. Villing & K. Zellweger
 */
public interface UserService extends ConfigurationCrudService<UserDto, UUID> {
}
