package hh.forest_of_habits.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
public class JwtTokenUtils {

    private static final String USER_NAME = "User name";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtDuration;

    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .subject("JWT Auth token")
                .expiration(new Date(new Date().getTime() + jwtDuration.toMillis()))
                .signWith(Keys.password(secret.toCharArray()))
                .claim(USER_NAME, userDetails.getUsername())
                .compact();
    }

    public String getUsername(String token) {

        Jws<Claims> jws = Jwts.parser()
                .verifyWith(Keys.password(secret.toCharArray()))
                .build()
                .parseSignedClaims(token);

        return jws.getPayload().get(USER_NAME, String.class);
    }
}
