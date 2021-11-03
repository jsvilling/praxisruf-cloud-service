package ch.fhnw.ip6.praxisruf.commons.dto.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class UserDto {
    private UUID id;
    private String userName;
    private String password;
    private String role;
}
