package ch.fhnw.ip6.praxisruf.config.security;

import ch.fhnw.ip6.praxisruf.commons.config.security.JWTProperties;
import ch.fhnw.ip6.praxisruf.commons.config.security.filter.JWTTokenGeneratorFilter;
import ch.fhnw.ip6.praxisruf.commons.config.security.filter.JWTTokenValidatorFilter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;


@Configuration
@ConfigurationProperties(prefix = "praxis-intercom.api")
@Setter
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private String adminOrigin;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().cors().configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of(adminOrigin, "http://localhost:63342"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowCredentials(true);
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setExposedHeaders(Collections.singletonList("Authorization"));
            config.setMaxAge(3600L);
            return config;
        })
        .and().csrf().disable() //handled by jwt
            .addFilterBefore(new JWTTokenValidatorFilter(jwtProperties()), BasicAuthenticationFilter.class)
            .addFilterAfter(new JWTTokenGeneratorFilter(jwtProperties()), BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/api/users/login").authenticated()
                .antMatchers("/api/clients").hasAnyRole("ADMIN")
                .antMatchers("/api/configurations").hasAnyRole("ADMIN")
                .antMatchers("/api/notificationtypes").hasAnyRole("ADMIN")
                .antMatchers("/api/users").hasAnyRole("ADMIN")
                .antMatchers("/api/notifications").hasAnyRole("ADMIN","USER")
                .antMatchers("/api/registrations").hasAnyRole("ADMIN","USER")
                .antMatchers("/api/speechsynthesis").hasAnyRole("ADMIN","USER")
                .and().httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JWTProperties jwtProperties() {
        return new JWTProperties();
    }
}
