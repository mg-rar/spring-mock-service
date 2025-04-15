package com.example.mock;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class Controller {
    private final Random random = new Random();

    private void delay() {
        try {
            long delay = (long)1000 + random.nextInt(1001);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @GetMapping("/get")
    public String getMethod() {
        delay();
        return "{\"login\":\"Login1\",\"status\":\"ok\"}";
    }

    @PostMapping(value = "/post", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> postMethod(@RequestBody Map<String, String> requestBody) {
        delay();

        String login = requestBody.get("login");
        String password = requestBody.get("password");
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put("login", login);
        response.put("password", password);
        response.put("date", date);

        return response;
    }
}
