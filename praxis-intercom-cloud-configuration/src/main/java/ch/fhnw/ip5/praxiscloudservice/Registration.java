package ch.fhnw.ip5.praxiscloudservice;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Registration {

    @Id
    private String clientId;
    private String fcmToken;

    /**
     * This no args constructor is required for JPA.
     * This should never be used explicitly.
     */
    protected Registration() { }

    public Registration(String clientId, String fcmToken) {
        this.clientId = clientId;
        this.fcmToken = fcmToken;
    }

    public String getClientId() {
        return clientId;
    }

    public String getFcmToken() {
        return fcmToken;
    }
}
