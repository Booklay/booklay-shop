package com.nhnacademy.booklay.server.controller.auth;

import com.nhnacademy.booklay.server.service.OauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/ouath/checked/user")
    public ResponseEntity<Void> checkOauthUser(@RequestParam String id, @RequestParam String email){

        oauthService.checkUser(id, email);

        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }
}
