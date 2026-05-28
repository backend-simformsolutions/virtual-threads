package com.goal.service;

import com.goal.entity.Employee;
import com.goal.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final Executor virtualThreadExecutor;
    private final EmployeeRepository employeeRepository;

    private static final String REPORTS_DIR = "reports";

    public void generateSimpleReport() throws IOException {
        log.info("Simple Report - Thread: {}", Thread.currentThread());

        long startTime = System.currentTimeMillis();

        List<Employee> employees = employeeRepository.findAll();
        writeEmployeesToCsv(employees, "simple");

        long endTime = System.currentTimeMillis();
        log.info("Simple Report - Time taken: {} ms,  File saved at: reports/employees_simple.csv", (endTime - startTime));
    }

    public void generateReportWithPlatformThreads() {
        Executor executor = Executors.newFixedThreadPool(5);
        executor.execute(() -> {
            log.info("Platform Thread Report - Thread: {}", Thread.currentThread());

            long startTime = System.currentTimeMillis();

            List<Employee> employees = employeeRepository.findAll();
            try {
                writeEmployeesToCsv(employees, "platform");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            long endTime = System.currentTimeMillis();
            log.info("Platform Thread Report - Time taken: {} ms, Thread: {}, File saved at: reports/employees_platform.csv", (endTime - startTime), Thread.currentThread());

        });
    }

    public void generateReportWithVirtualThreads() {
        virtualThreadExecutor.execute(() -> {
            log.info("Virtual Thread Report - Thread: {}", Thread.currentThread());

            long startTime = System.currentTimeMillis();

            List<Employee> employees = employeeRepository.findAll();
            try {
                writeEmployeesToCsv(employees, "virtual");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            long endTime = System.currentTimeMillis();
            log.info("Virtual Thread Report - Time taken: {} ms, Thread: {}, File saved at: reports/employees_virtual.csv", (endTime - startTime), Thread.currentThread());
        });
    }

    public static void writeEmployeesToCsv(List<Employee> employees, String reportType) throws IOException {
        String fileName = String.format("employees_%s.csv", reportType);
        Path filePath = Paths.get(REPORTS_DIR).resolve(fileName);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("ID", "First Name", "Last Name", "Email", "Department", "Salary"))) {

            for (Employee employee : employees) {
                csvPrinter.printRecord(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getEmail(), employee.getDepartment(), employee.getSalary());
            }
        }
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
}

