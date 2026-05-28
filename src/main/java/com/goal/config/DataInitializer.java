package com.goal.config;

import com.goal.entity.Employee;
import com.goal.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) {
        if (employeeRepository.count() == 0) {
            log.info("Initializing employee data...");

            List<Employee> employees = new ArrayList<>();

            employees.add(new Employee(null, "John", "Doe", "john.doe@example.com",
                    "Engineering", new BigDecimal("75000.00")));

            employees.add(new Employee(null, "Jane", "Smith", "jane.smith@example.com",
                    "Marketing", new BigDecimal("68000.00")));

            employees.add(new Employee(null, "Michael", "Johnson", "michael.johnson@example.com",
                    "Engineering", new BigDecimal("82000.00")));

            employees.add(new Employee(null, "Emily", "Davis", "emily.davis@example.com",
                    "HR", new BigDecimal("65000.00")));

            employees.add(new Employee(null, "David", "Wilson", "david.wilson@example.com",
                    "Sales", new BigDecimal("72000.00")));

            employees.add(new Employee(null, "Sarah", "Brown", "sarah.brown@example.com",
                    "Engineering", new BigDecimal("88000.00")));

            employees.add(new Employee(null, "James", "Taylor", "james.taylor@example.com",
                    "Finance", new BigDecimal("78000.00")));

            employees.add(new Employee(null, "Linda", "Anderson", "linda.anderson@example.com",
                    "Marketing", new BigDecimal("71000.00")));

            employees.add(new Employee(null, "Robert", "Thomas", "robert.thomas@example.com",
                    "Engineering", new BigDecimal("85000.00")));

            employees.add(new Employee(null, "Jennifer", "Martinez", "jennifer.martinez@example.com",
                    "HR", new BigDecimal("69000.00")));

            employees.add(new Employee(null, "William", "Garcia", "william.garcia@example.com",
                    "Engineering", new BigDecimal("91000.00")));

            employees.add(new Employee(null, "Patricia", "Rodriguez", "patricia.rodriguez@example.com",
                    "Sales", new BigDecimal("73000.00")));

            employees.add(new Employee(null, "Richard", "Martinez", "richard.martinez@example.com",
                    "Finance", new BigDecimal("80000.00")));

            employees.add(new Employee(null, "Barbara", "Hernandez", "barbara.hernandez@example.com",
                    "Marketing", new BigDecimal("67000.00")));

            employees.add(new Employee(null, "Charles", "Lopez", "charles.lopez@example.com",
                    "Engineering", new BigDecimal("86000.00")));

            employees.add(new Employee(null, "Susan", "Gonzalez", "susan.gonzalez@example.com",
                    "HR", new BigDecimal("64000.00")));

            employees.add(new Employee(null, "Joseph", "Wilson", "joseph.wilson@example.com",
                    "Engineering", new BigDecimal("89000.00")));

            employees.add(new Employee(null, "Jessica", "Anderson", "jessica.anderson@example.com",
                    "Sales", new BigDecimal("74000.00")));

            employees.add(new Employee(null, "Thomas", "Thomas", "thomas.thomas@example.com",
                    "Finance", new BigDecimal("81000.00")));

            employees.add(new Employee(null, "Karen", "Taylor", "karen.taylor@example.com",
                    "Marketing", new BigDecimal("70000.00")));

            employees.add(new Employee(null, "Christopher", "Moore", "christopher.moore@example.com",
                    "Engineering", new BigDecimal("92000.00")));

            employees.add(new Employee(null, "Nancy", "Jackson", "nancy.jackson@example.com",
                    "HR", new BigDecimal("66000.00")));

            employees.add(new Employee(null, "Daniel", "Martin", "daniel.martin@example.com",
                    "Engineering", new BigDecimal("87000.00")));

            employees.add(new Employee(null, "Lisa", "Lee", "lisa.lee@example.com",
                    "Sales", new BigDecimal("76000.00")));

            employees.add(new Employee(null, "Matthew", "Perez", "matthew.perez@example.com",
                    "Finance", new BigDecimal("83000.00")));

            employees.add(new Employee(null, "Betty", "Thompson", "betty.thompson@example.com",
                    "Marketing", new BigDecimal("68000.00")));

            employees.add(new Employee(null, "Donald", "White", "donald.white@example.com",
                    "Engineering", new BigDecimal("90000.00")));

            employees.add(new Employee(null, "Margaret", "Harris", "margaret.harris@example.com",
                    "HR", new BigDecimal("67000.00")));

            employees.add(new Employee(null, "Anthony", "Sanchez", "anthony.sanchez@example.com",
                    "Engineering", new BigDecimal("88000.00")));

            employees.add(new Employee(null, "Sandra", "Clark", "sandra.clark@example.com",
                    "Sales", new BigDecimal("75000.00")));

            employees.add(new Employee(null, "Mark", "Ramirez", "mark.ramirez@example.com",
                    "Finance", new BigDecimal("79000.00")));

            employees.add(new Employee(null, "Ashley", "Lewis", "ashley.lewis@example.com",
                    "Marketing", new BigDecimal("69000.00")));

            employees.add(new Employee(null, "Steven", "Robinson", "steven.robinson@example.com",
                    "Engineering", new BigDecimal("93000.00")));

            employees.add(new Employee(null, "Kimberly", "Walker", "kimberly.walker@example.com",
                    "HR", new BigDecimal("65000.00")));

            employees.add(new Employee(null, "Paul", "Young", "paul.young@example.com",
                    "Engineering", new BigDecimal("84000.00")));

            employees.add(new Employee(null, "Donna", "Allen", "donna.allen@example.com",
                    "Sales", new BigDecimal("77000.00")));

            employees.add(new Employee(null, "Joshua", "King", "joshua.king@example.com",
                    "Finance", new BigDecimal("82000.00")));

            employees.add(new Employee(null, "Carol", "Wright", "carol.wright@example.com",
                    "Marketing", new BigDecimal("71000.00")));

            employees.add(new Employee(null, "Andrew", "Scott", "andrew.scott@example.com",
                    "Engineering", new BigDecimal("91000.00")));

            employees.add(new Employee(null, "Michelle", "Torres", "michelle.torres@example.com",
                    "HR", new BigDecimal("68000.00")));

            employees.add(new Employee(null, "Kenneth", "Nguyen", "kenneth.nguyen@example.com",
                    "Engineering", new BigDecimal("89000.00")));

            employees.add(new Employee(null, "Dorothy", "Hill", "dorothy.hill@example.com",
                    "Sales", new BigDecimal("73000.00")));

            employees.add(new Employee(null, "Kevin", "Flores", "kevin.flores@example.com",
                    "Finance", new BigDecimal("80000.00")));

            employees.add(new Employee(null, "Amanda", "Green", "amanda.green@example.com",
                    "Marketing", new BigDecimal("72000.00")));

            employees.add(new Employee(null, "Brian", "Adams", "brian.adams@example.com",
                    "Engineering", new BigDecimal("94000.00")));

            employees.add(new Employee(null, "Melissa", "Nelson", "melissa.nelson@example.com",
                    "HR", new BigDecimal("66000.00")));

            employees.add(new Employee(null, "George", "Baker", "george.baker@example.com",
                    "Engineering", new BigDecimal("87000.00")));

            employees.add(new Employee(null, "Stephanie", "Hall", "stephanie.hall@example.com",
                    "Sales", new BigDecimal("78000.00")));

            employees.add(new Employee(null, "Edward", "Rivera", "edward.rivera@example.com",
                    "Finance", new BigDecimal("81000.00")));

            employees.add(new Employee(null, "Rebecca", "Campbell", "rebecca.campbell@example.com",
                    "Marketing", new BigDecimal("70000.00")));

            employeeRepository.saveAll(employees);

            log.info("Initialized {} employees", employees.size());
        } else {
            log.info("Employee data already exists, skipping initialization");
        }
    }
}

