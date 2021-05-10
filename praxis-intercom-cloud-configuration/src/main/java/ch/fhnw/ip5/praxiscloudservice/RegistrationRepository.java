package ch.fhnw.ip5.praxiscloudservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, String> {

    List<Registration> findAll();

    Registration save(Registration registration);

}
