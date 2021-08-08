package ch.fhnw.ip5.praxiscloudservice.persistence.configuration;

import ch.fhnw.ip5.praxiscloudservice.domain.configuration.RuleParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RuleParametersRepository extends JpaRepository<RuleParameters, UUID> {
}
