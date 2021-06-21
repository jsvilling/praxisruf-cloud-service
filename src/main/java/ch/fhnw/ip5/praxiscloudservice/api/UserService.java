package ch.fhnw.ip5.praxiscloudservice.api;

import java.util.UUID;

public interface UserService {

    UUID register(String userName);

    UUID login(String userName);

    void logout(String userName);

}
