package com.nhnacademy.booklay.server.config;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import javax.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
public class JwtConfig {

    @Value(("${booklay.jwt.secret}"))
    private final String secret;

    @Bean
    public JwtParser jwtParser() {
        return Jwts.parserBuilder()
            .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
