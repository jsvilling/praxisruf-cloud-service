package ch.fhnw.ip5.praxiscloudservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, String> {

    List<Registration> findAll();

    Registration save(Registration registration);

}
