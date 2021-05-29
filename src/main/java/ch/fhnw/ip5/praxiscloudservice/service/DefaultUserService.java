package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.api.UserService;
import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.User;
import ch.fhnw.ip5.praxiscloudservice.persistence.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public UUID login(String userName) {
        log.debug("Login " + userName);
        return userRepository
                .findByName(userName)
                .map(User::getId)
                .orElseThrow(() -> new PraxisIntercomException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public void logout(String userName) {
        log.debug("Logout " + userName);
    }
}
