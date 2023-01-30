package com.nhnacademy.booklay.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
    private boolean healthStatus = true;

    @GetMapping
    public ResponseEntity<String> healthCheck() {
        if (healthStatus) {
            return ResponseEntity.ok("OK");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("BAD_REQUEST");
    }

    @GetMapping("/deploy/ready")
    public void makeHealthCheckFail() {
        this.healthStatus = false;
    }

}
