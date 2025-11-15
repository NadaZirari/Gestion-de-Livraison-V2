package com.delivrey.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test", description = "API de test")
public class TestController {
    
    @GetMapping(produces = "text/plain")
    @Operation(summary = "Endpoint de test simple")
    public String test() {
        return "OK";
    }
}
