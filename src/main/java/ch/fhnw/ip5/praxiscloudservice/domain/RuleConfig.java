package ch.fhnw.ip5.praxiscloudservice.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Getter
public class RuleConfig {

    @Id
    private UUID id;

    private RuleType type;

    private String value;

    @ManyToOne
    @JoinColumn(name = "id")
    private ClientConfiguration clientConfiguration;
}
