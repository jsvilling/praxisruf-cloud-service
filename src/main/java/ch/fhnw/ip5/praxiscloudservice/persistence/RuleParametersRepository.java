package ch.fhnw.ip5.praxiscloudservice.persistence;

import ch.fhnw.ip5.praxiscloudservice.domain.RuleParameters;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RuleParametersRepository extends JpaRepository<RuleParameters, UUID> {
}
