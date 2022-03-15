package ch.fhnw.ip6.praxisruf.signaling.domain;

import lombok.*;

/**
 * Represents a signaling message being exchanged with the {@link ch.fhnw.ip6.praxisruf.signaling.service.SignalingService}.
 *
 * The values sender and recipient are used to forward the signal over known connections in the {@link ch.fhnw.ip6.praxisruf.signaling.service.ConnectionRegistry}.
 * The value notificationOnFailedDelivery is used to determine wheter a recipient should be informed via notifications if delivery of a signal has failed.
 *
 * @author J. Villing
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Signal {

    private String sender;
    private String recipient;
    private String type;
    private String payload;
    private String description;
    private boolean notificationOnFailedDelivery;

}
