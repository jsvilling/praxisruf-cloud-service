package ch.fhnw.ip6.praxisruf.configuration.persistence;

import ch.fhnw.ip6.praxisruf.configuration.domain.CallGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface CallGroupRepository extends JpaRepository<CallGroup, UUID> {

}
