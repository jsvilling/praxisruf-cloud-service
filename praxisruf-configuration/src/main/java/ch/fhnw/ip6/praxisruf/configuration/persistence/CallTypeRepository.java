package ch.fhnw.ip6.praxisruf.configuration.persistence;

import ch.fhnw.ip6.praxisruf.configuration.domain.CallType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CallTypeRepository extends JpaRepository<CallType, UUID> {

}
