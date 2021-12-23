package ch.fhnw.ip6.praxisruf.domain;

import lombok.*;

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
}
