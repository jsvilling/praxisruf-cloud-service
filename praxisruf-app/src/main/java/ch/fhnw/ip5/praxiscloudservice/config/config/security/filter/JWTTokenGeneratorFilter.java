package ch.fhnw.ip5.praxiscloudservice.config.config.security.filter;

import ch.fhnw.ip5.praxiscloudservice.config.config.security.JWTProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

    private final JWTProperties jwtProperties;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication) {
            Date today = new Date();
            SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder().setIssuer("Praxis Intercom").setSubject("JWT Token")
                             .claim("username", authentication.getName())
                             .claim("authorities", populateAuthorities(authentication.getAuthorities()))
                             .claim("userId",authentication.getDetails())
                             .setIssuedAt(today)
                             .setExpiration(new Date(today.getTime() + (1000*60*60*24)))
                             .signWith(key).compact();
            response.setHeader("Authorization", jwt);
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/api/users/login");
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}
