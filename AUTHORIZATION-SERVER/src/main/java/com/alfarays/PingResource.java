package com.alfarays;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingResource {

    @GetMapping("/ping")
    public String ping() {
        return "Ping response from authorization server";
    }

}
