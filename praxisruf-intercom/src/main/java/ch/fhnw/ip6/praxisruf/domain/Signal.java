package ch.fhnw.ip6.praxisruf.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Signal {
    private String sender;
    private String recipient;
    private String type;
    private String payload;
}
