package com.estoque.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health")
@RestController
@RequestMapping("/api")
public class HealthController {
    @Operation(summary = "Verificar se a API está no ar")
    @GetMapping("/health")
    public String health() {
        return "ok";
    }
}
