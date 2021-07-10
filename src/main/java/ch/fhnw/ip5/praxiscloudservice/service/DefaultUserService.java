package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.api.UserService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.UserDto;
import ch.fhnw.ip5.praxiscloudservice.api.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.domain.PraxisIntercomUser;
import ch.fhnw.ip5.praxiscloudservice.domain.UserRole;
import ch.fhnw.ip5.praxiscloudservice.persistence.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultUserService implements UserService, UserDetailsService, AuthenticationProvider {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public UUID register(UserDto userDto) {
        final PraxisIntercomUser user = PraxisIntercomUser
                .builder()
                .id(UUID.randomUUID())
                .userName(userDto.getUserName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(UserRole.valueOf(userDto.getRole()))
                .build();
        return userRepository.save(user).getId();
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream().map(user ->
                        UserDto.builder()
                                .id(user.getId())
                                .userName(user.getUsername())
                                .build())
                .collect(Collectors.toList());
    }

    @Override
    public UUID updateUser(UserDto userDto) {
        PraxisIntercomUser user = userRepository.findByUserName(userDto.getUserName()).orElseThrow(() -> new PraxisIntercomException(ErrorCode.USER_NOT_FOUND));
        user = userRepository.save(PraxisIntercomUser.builder()
                .id(user.getId())
                .userName(userDto.getUserName())
                .role(UserRole.valueOf(userDto.getRole()))
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build());
        return user.getId();
    }

    //Internal Spring Security Method
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(userName));
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        PraxisIntercomUser user = userRepository.findByUserName(userName).orElseThrow(() -> new BadCredentialsException("No User registered with: " + userName));
        if(passwordEncoder.matches(pwd, user.getPassword())){
            return new UsernamePasswordAuthenticationToken(userName,pwd, user.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid password!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
