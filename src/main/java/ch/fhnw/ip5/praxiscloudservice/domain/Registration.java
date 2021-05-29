package ch.fhnw.ip5.praxiscloudservice.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Getter
public class Registration {

    @Id
    private UUID clientId;

    private String fcmToken;
}
