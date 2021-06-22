package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.api.UserService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.UserDto;
import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisIntercomUser;
import ch.fhnw.ip5.praxiscloudservice.persistence.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public UUID register(String userName) {
        final PraxisIntercomUser user = PraxisIntercomUser
                .builder()
                .id(UUID.randomUUID())
                .name(userName)
                .build();
        return userRepository.save(user).getId();
    }

    @Override
    public UUID login(String userName) {
        log.debug("Login " + userName);
        return userRepository
                .findByName(userName)
                .map(PraxisIntercomUser::getId)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public void logout(String userName) {
        log.debug("Logout " + userName);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream().map(user ->
                        UserDto.builder()
                                .id(user.getId())
                                .userName(user.getName())
                                .build())
                .collect(Collectors.toList());
    }
}
