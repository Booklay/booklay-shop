package com.nhnacademy.booklay.server.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 기본 설정
 *
 * @author 조현진, 양승아
 */

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
            .anyRequest()
            .permitAll();

        http.csrf()
            .disable();

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                         .antMatchers("/resources/**", "/static/**", "/webjars/**", "/img/**", "/css/**", "/js/**",
                                      "/scss/**", "/plugins/**", "/mail/**");
    }


}
