package ch.fhnw.ip5.praxiscloudservice.api;

import ch.fhnw.ip5.praxiscloudservice.api.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UUID register(UserDto user);

    List<UserDto> findAllUsers();

    UUID updateUser(UserDto userName);
}
