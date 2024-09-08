package org.jyj.status.controller;

import org.jyj.common.response.GenericResponse;
import org.jyj.status.response.HealthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @GetMapping("/health")
    public GenericResponse<HealthResponse> health() {
        return new GenericResponse<>("서버 상태", new HealthResponse());
    }

}


