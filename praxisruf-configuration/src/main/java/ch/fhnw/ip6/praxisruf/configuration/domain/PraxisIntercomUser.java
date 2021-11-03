package ch.fhnw.ip6.praxisruf.configuration.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * Represents a user.
 */
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE) // for Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
@Builder
public class PraxisIntercomUser implements UserDetails {

    @Id
    @GeneratedValue
    @Getter
    private UUID id;

    private String userName;

    private String password;

    @Getter
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
