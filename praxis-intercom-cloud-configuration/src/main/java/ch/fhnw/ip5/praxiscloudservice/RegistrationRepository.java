package ch.fhnw.ip5.praxiscloudservice;

import java.util.Set;

public interface RegistrationRepository {

    Set<Registration> getAll();

    Registration save(Registration registration);

}
