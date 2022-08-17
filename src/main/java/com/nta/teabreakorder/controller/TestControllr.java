package com.nta.teabreakorder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestControllr {
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String gen(@RequestParam("p") String p) {
        return passwordEncoder.encode(p);
    }


    @GetMapping("/ping")
    public String ping() {
        return "OK";
    }
}
