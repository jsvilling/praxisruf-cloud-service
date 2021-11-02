package ch.fhnw.ip5.praxiscloudservice.configuration.persistence;

import ch.fhnw.ip5.praxiscloudservice.configuration.domain.RuleParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RuleParametersRepository extends JpaRepository<RuleParameters, UUID> {
}
