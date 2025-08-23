package com.example.demo.Controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.SeedTemplatesDTO;
import com.example.demo.Service.InstallmentTemplateService;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/installment-templates")
@RequiredArgsConstructor
public class InstallmentTemplateController  {
	private final InstallmentTemplateService service;

    @PostMapping("/seed-default")
    public ResponseEntity<Map<String,Object>> seedDefault(@RequestBody SeedTemplatesDTO dto) {
        service.seedDefaultTemplates(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Templates created (1time/2time/3time)"));
}
    }
