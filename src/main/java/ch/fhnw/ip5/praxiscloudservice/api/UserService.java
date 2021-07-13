package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.api.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto register(UserDto user);

    List<UserDto> findAllUsers();

    UserDto updateUser(UserDto userName);

    UserDto findUserById(UUID id);

    void deleteById(UUID id);

    void deleteAllById(List<UUID> ids);
}
