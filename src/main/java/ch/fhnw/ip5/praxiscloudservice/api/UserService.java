package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.api.dto.UserDto;

import java.util.List;
import java.util.UUID;

/**
 * This interface specifies interactions with Client entities.
 *
 * @author J. Villing & K. Zellweger
 */
public interface UserService {

    /**
     * Creates a new PraxisIntercomUser according to the given UserDto.
     *
     * @param userDto
     * @throws PraxisIntercomException - If the given UserDto is invalid
     * @return UserDto
     */
    UserDto register(UserDto userDto);

    /**
     * Finds all registered Users.
     *
     * @return List<UserDto>
     */
    List<UserDto> findAllUsers();

    /**
     * Updates a PraxisIntercomUser according to the given UserDto.
     *
     * The property id of the given userDto is relevant for determining which PraxisIntercomUser is updated.
     *
     * @param userDto
     * @throws PraxisIntercomException - If the given UserDto is invalid
     * @return UserDto
     */
    UserDto updateUser(UserDto userDto);

    /**
     * Finds the PraxisIntercomUser with the given userId
     *
     * @param id
     * @throws PraxisIntercomException - If no User with the given id exists.
     * @return
     */
    UserDto findUserById(UUID id);

    /**
     * Deletes the PraxisIntercomUser with the given id.
     *
     * If no PraxisIntercomUser with the given id exists, it is assumed that the user was already deleted. In this case
     * the operation terminates silently.
     *
     * @param id
     */
    void deleteById(UUID id);

    /**
     * Deletes the PraxisIntercomUser with the given id.
     *
     * If the deletion of any of the given ids fails, the process will be aborted and the exception is re-thrown.
     *
     * @param id
     */
    void deleteAllById(List<UUID> ids);
}
