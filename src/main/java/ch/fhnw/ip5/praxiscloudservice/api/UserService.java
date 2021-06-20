package ch.fhnw.ip5.praxiscloudservice.api;

import java.util.UUID;

public interface UserService {

    UUID login(String userName);

    void logout(String userName);

}
