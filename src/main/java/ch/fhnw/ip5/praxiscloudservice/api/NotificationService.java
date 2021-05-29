package ch.fhnw.ip5.praxiscloudservice.api;

/**
 * This interface specifies contracts for sending messages to client devices.
 *
 * @author J. Villing
 */
public interface NotificationService {

    void send(String token) throws Exception;

    void sendAll() throws Exception;

}
