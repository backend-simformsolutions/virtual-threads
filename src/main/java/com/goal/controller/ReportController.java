package com.goal.controller;

import com.goal.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final EmployeeService employeeService;

/*
    Check performance with Apache Bench (ab) tool:
    ab -n 300 -c 100 http://localhost:8081/api/reports/simple
    ab -n 300 -c 100 http://localhost:8081/api/reports/platform
    ab -n 300 -c 100 http://localhost:8081/api/reports/virtual
*/

    @GetMapping("/simple")
    public ResponseEntity<Map<String, String>> generateSimpleReport() throws IOException {
        log.info("=== Simple Report Request ===");

        employeeService.generateSimpleReport();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Report generated successfully");
        response.put("filePath", "reports/employees_simple.csv");
        response.put("reportType", "simple");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/platform")
    public ResponseEntity<Map<String, String>> generateReportWithPlatformThread() {
        log.info("=== Platform Thread Report Request ===");

        employeeService.generateReportWithPlatformThreads();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Report generated successfully using platform threads");
        response.put("filePath", "reports/employees_platform.csv");
        response.put("reportType", "platform");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/virtual")
    public ResponseEntity<Map<String, String>> generateReportWithVirtualThread() {
        log.info("=== Virtual Thread Report Request ===");

        employeeService.generateReportWithVirtualThreads();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Report generated successfully using virtual threads");
        response.put("filePath", "reports/employees_virtual.csv");
        response.put("reportType", "virtual");

        return ResponseEntity.ok(response);
    }
}

