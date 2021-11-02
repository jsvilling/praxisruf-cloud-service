package ch.fhnw.ip5.praxiscloudservice.configuration.service;

import ch.fhnw.ip5.praxiscloudservice.commons.dto.configuration.UserDto;
import ch.fhnw.ip5.praxiscloudservice.commons.exception.ErrorCode;
import ch.fhnw.ip5.praxiscloudservice.commons.exception.PraxisIntercomException;
import ch.fhnw.ip5.praxiscloudservice.configuration.api.UserService;
import ch.fhnw.ip5.praxiscloudservice.configuration.domain.PraxisIntercomUser;
import ch.fhnw.ip5.praxiscloudservice.configuration.domain.UserRole;
import ch.fhnw.ip5.praxiscloudservice.configuration.persistence.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public UserDto create(UserDto userDto) {
        final PraxisIntercomUser user = PraxisIntercomUser
                .builder()
                .id(UUID.randomUUID())
                .userName(userDto.getUserName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(UserRole.valueOf(userDto.getRole()))
                .build();
        userRepository.save(user);
        return UserDto.builder().userName(user.getUsername()).role(user.getRole().name()).password(user.getPassword()).id(user.getId()).build();
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                             .stream().map(user ->
                                                   UserDto.builder()
                                                          .id(user.getId())
                                                          .role(user.getRole().name())
                                                          .password(user.getPassword())
                                                          .userName(user.getUsername())
                                                          .build())
                             .collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserDto userDto) {
        PraxisIntercomUser user = userRepository.findById(userDto.getId()).orElseThrow(
                () -> new PraxisIntercomException(ErrorCode.USER_NOT_FOUND));
        String password = userDto.getPassword() == null ? user.getPassword() : userDto.getPassword();
        if (!(password.startsWith(BCryptVersion.$2A.getVersion()) || password
                .startsWith(BCryptVersion.$2B.getVersion()) || password.startsWith(BCryptVersion.$2Y.getVersion()))) {
            password = passwordEncoder.encode(password);
        }
        user = userRepository.save(PraxisIntercomUser.builder()
                                                     .id(user.getId())
                                                     .userName(userDto.getUserName())
                                                     .role(UserRole.valueOf(userDto.getRole()))
                                                     .password(password)
                                                     .build());
        return UserDto.builder().userName(user.getUsername()).password(user.getPassword()).role(user.getRole().name())
                      .id(user.getId()).build();
    }

    @Override
    public UserDto findById(UUID id) {
        PraxisIntercomUser user = userRepository.findById(id).orElseThrow(
                () -> new PraxisIntercomException(ErrorCode.USER_NOT_FOUND));
        return UserDto.builder().userName(user.getUsername()).password(user.getPassword()).role(user.getRole().name()).id(user.getId()).build();
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteAllById(List<UUID> ids) {
        ids.forEach(this::deleteById);
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
        PraxisIntercomUser user = userRepository.findByUserName(userName).orElseThrow(
                () -> new BadCredentialsException("No User registered with: " + userName));
        if (passwordEncoder.matches(pwd, user.getPassword())) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, pwd, user.getAuthorities());
            authenticationToken.setDetails(user.getId());
            return authenticationToken;
        } else {
            throw new BadCredentialsException("Invalid password!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
