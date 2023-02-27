package com.nhnacademy.booklay.server.controller;

import com.nhnacademy.booklay.server.utils.TestCacheClear;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("test")
public class CacheTestController {
    private final TestCacheClear testCacheClear;
    private final RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<List<int[]>> response() {
        List<Long> testProductArray = List.of(34L, 35L, 36L, 37L, 38L);
        List<int[]> times = new ArrayList<>();
        for (Long productNo : testProductArray) {
            times.add(new int[20]);
            for (int i = 0; i< 20; i++){
                LocalDateTime startTime = LocalDateTime.now();
                restTemplate.getForObject("http://localhost:6060/product/view/" + productNo,
                    String.class);
                LocalDateTime endTime = LocalDateTime.now();
                testCacheClear.cacheDelete();
                int time =
                    (endTime.getSecond() - startTime.getSecond()) * 1000000000+(endTime.getNano()-startTime.getNano());
                times.get((int) (productNo-34L))[i] = time/1000000;

            }
        }
        return ResponseEntity.ok(times);
    }
    @GetMapping("cache")
    public ResponseEntity<List<int[]>> responseCache() {
        List<Long> testProductArray = List.of(34L, 35L, 36L, 37L, 38L);
        List<int[]> times = new ArrayList<>();
        for (Long productNo : testProductArray) {
            times.add(new int[20]);
            restTemplate.getForObject("http://localhost:6060/product/view/" + productNo,
                String.class);
            for (int i = 0; i< 20; i++){
                LocalDateTime startTime = LocalDateTime.now();
                restTemplate.getForObject("http://localhost:6060/product/view/" + productNo,
                    String.class);
                LocalDateTime endTime = LocalDateTime.now();
                int time =
                    (endTime.getSecond() - startTime.getSecond()) * 1000000000+(endTime.getNano()-startTime.getNano());
                times.get((int) (productNo-34L))[i] = time/1000000;

            }
        }
        return ResponseEntity.ok(times);
    }

}
