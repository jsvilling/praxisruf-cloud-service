package ch.fhnw.ip5.praxiscloudservice.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Registration {

    @Id
    private UUID clientId;
    private String fcmToken;

    /**
     * This no args constructor is required for JPA.
     * This should never be used explicitly.
     */
    protected Registration() { }

    public Registration(UUID clientId, String fcmToken) {
        this.clientId = clientId;
        this.fcmToken = fcmToken;
    }

    public UUID getClientId() {
        return clientId;
    }

    public String getFcmToken() {
        return fcmToken;
    }
}
