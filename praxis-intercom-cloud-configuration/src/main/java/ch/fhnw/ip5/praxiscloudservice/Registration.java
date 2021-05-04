package ch.fhnw.ip5.praxiscloudservice;

public class Registration {

    private final String clientId;
    private final String fcmToken;

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
