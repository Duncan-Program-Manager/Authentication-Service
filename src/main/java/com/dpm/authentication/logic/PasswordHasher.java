package com.dpm.authentication.logic;

import com.dpm.authentication.datamodels.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.*;


@Component
public class PasswordHasher {

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String createJWT(User user) {

        Map<String, Object> claims = new HashMap<>();
        // to add the role in the claims
        //claims.put("role",user.getRole().toString());
        claims.put("id",user.getId().toString());


        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("tester_key_is_a_good_key");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setSubject(user.getEmail())
                .setIssuer(user.getUsername())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(6).toInstant()))
                .signWith(signatureAlgorithm, signingKey);

        //Builds the JWT and serializes it to a compact, URL-safe string

        return builder.compact();
    }

    public boolean validateToken(String token) {
        boolean validation = false;
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey("tester_key_is_a_good_key").parseClaimsJws(token);
            validation = !claims.getBody().getExpiration().before(new java.util.Date());

        } catch (ExpiredJwtException e) {
            System.out.println(" Token expired ");
        } catch (Exception e) {
            System.out.println(" Some other exception in JWT parsing ");
        }
        return validation;
    }

}
