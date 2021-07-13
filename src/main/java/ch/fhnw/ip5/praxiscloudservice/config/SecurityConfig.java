package ch.fhnw.ip5.praxiscloudservice.config;

import ch.fhnw.ip5.praxiscloudservice.filter.JWTTokenGeneratorFilter;
import ch.fhnw.ip5.praxiscloudservice.filter.JWTTokenValidatorFilter;
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
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .cors().configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("*")); //TODO: add production URL
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowCredentials(true);
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setExposedHeaders(Collections.singletonList("Authorization"));
            config.setMaxAge(3600L);
            return config;
        }).and().csrf().disable() //handled by jwt
            .addFilterBefore(new JWTTokenValidatorFilter(jwtProperties()), BasicAuthenticationFilter.class)
            .addFilterAfter(new JWTTokenGeneratorFilter(jwtProperties()), BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/api/users/login").authenticated()
                .antMatchers("/api/users").authenticated()
                .antMatchers("/api/clients/all").hasAnyRole("ADMIN","USER")//TODO: authorize endpoints by Role
                .and().httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean JWTProperties jwtProperties() {return new JWTProperties();}
}
