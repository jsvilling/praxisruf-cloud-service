package ch.fhnw.ip6.praxisruf.commons.config.security.filter;

import ch.fhnw.ip6.praxisruf.commons.config.security.JWTProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@AllArgsConstructor
public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    private final JWTProperties jwtProperties;

    public void doFilterInternal(ServerHttpRequest request, ServerHttpResponse response) throws ServletException, IOException {
        final String jwt = request.getHeaders().get("Authorization").get(0);
        checkAuth(jwt);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final String jwt = request.getHeader("Authorization");
        checkAuth(jwt);
        chain.doFilter(request, response);
    }

    private void checkAuth(String jwt) {
        if (null != jwt) {
            if(jwt.contains("Bearer ")){jwt = jwt.replace("Bearer ", "");}
            try {
                SecretKey key = Keys.hmacShaKeyFor(
                        jwtProperties.getKey().getBytes(StandardCharsets.UTF_8));

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String username = String.valueOf(claims.get("username"));
                String authorities = (String) claims.get("authorities");
                UUID userId = UUID.fromString(String.valueOf(claims.get("userId")));
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils
                        .commaSeparatedStringToAuthorityList(authorities));

                authenticationToken.setDetails(userId);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (Exception e) {
                throw new BadCredentialsException("Invalid Token received!");
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/api/users/login");
    }
}
