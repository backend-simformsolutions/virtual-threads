# Virtual Threads POC — Report Service

A Spring Boot proof-of-concept that demonstrates how **Java 21 Virtual Threads** compare against traditional **Platform Threads** and a **simple (single-threaded)** approach when performing I/O-bound work — specifically, fetching employee records from a database and writing them to a CSV file.

---

## Table of Contents

- [What Are Virtual Threads?](#what-are-virtual-threads)
- [Project Overview](#project-overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [How Each Approach Works](#how-each-approach-works)
- [Performance Benchmarking](#performance-benchmarking)
- [Key Observations](#key-observations)

---

## What Are Virtual Threads?

Virtual Threads (introduced as a preview in Java 19 and made stable in **Java 21** via [JEP 444](https://openjdk.org/jeps/444)) are lightweight threads managed by the JVM rather than the OS.

| Feature | Platform Thread | Virtual Thread |
|---|---|---|
| Managed by | OS | JVM |
| Memory footprint | ~1 MB per thread | ~few KB per thread |
| Blocking behaviour | Blocks the OS thread | Parks the virtual thread; OS thread is freed |
| Creation cost | High | Very low |
| Best suited for | CPU-bound work | I/O-bound work |

When a virtual thread performs a blocking I/O operation (e.g. a database query, file write), the JVM **unmounts** it from its carrier (OS) thread, freeing that OS thread to run other virtual threads. Once the I/O completes the virtual thread is remounted and execution resumes. This allows millions of concurrent tasks without the overhead of millions of OS threads.

---

## Project Overview

The service pre-loads **50 employee records** into an in-memory H2 database on startup. Three report endpoints each demonstrate a different concurrency strategy:

1. **Simple** — generates the CSV report synchronously on the request-handling thread.
2. **Platform Threads** — offloads the work to a fixed thread pool of 5 OS-level threads (`Executors.newFixedThreadPool(5)`).
3. **Virtual Threads** — offloads the work to a virtual-thread-per-task executor (`Executors.newVirtualThreadPerTaskExecutor()`).

Each approach:
- Fetches all employees from the H2 database (I/O operation).
- Writes them to a CSV file under the `reports/` directory (I/O operation).
- Logs the executing thread name and time taken.

---

## Tech Stack

| Technology | Version |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.3 |
| Spring Data JPA | (via Boot starter) |
| H2 Database | In-memory (runtime) |
| Apache Commons CSV | 1.10.0 |
| Lombok | Latest |
| Maven | 3.x |

---

## Project Structure

```
report-service/
├── reports/                          # Generated CSV reports land here
│   ├── employees_simple.csv
│   ├── employees_platform.csv
│   └── employees_virtual.csv
└── src/main/java/com/goal/
    ├── ReportServiceApplication.java # Spring Boot entry point
    ├── config/
    │   ├── DataInitializer.java      # Seeds 50 employees on startup
    │   └── VirtualThreadExecutorConfig.java  # Registers virtual-thread executor bean
    ├── controller/
    │   ├── ReportController.java     # 3 report endpoints (simple / platform / virtual)
    │   └── EmployeeController.java   # CRUD endpoints for employees
    ├── entity/
    │   └── Employee.java             # JPA entity (id, firstName, lastName, email, department, salary)
    ├── repository/
    │   └── EmployeeRepository.java   # Spring Data JPA repository
    └── service/
        └── EmployeeService.java      # Core logic: DB fetch + CSV write for each strategy
```

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.6+

### Run the Application

```bash
./mvnw spring-boot:run
```

The application starts on **port 8081**.

### H2 Console

Browse the in-memory database at:

```
http://localhost:8081/h2-console
```

| Setting | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:reportdb` |
| Username | `sa` |
| Password | *(empty)* |

---

## API Endpoints

### Report Endpoints

| Method | URL | Description | Output File |
|---|---|---|---|
| `GET` | `/api/reports/simple` | Generate CSV on the calling thread (no extra threads) | `reports/employees_simple.csv` |
| `GET` | `/api/reports/platform` | Generate CSV using a platform thread pool (5 threads) | `reports/employees_platform.csv` |
| `GET` | `/api/reports/virtual` | Generate CSV using a virtual thread | `reports/employees_virtual.csv` |

**Sample response (all three return the same shape):**

```json
{
  "message": "Report generated successfully",
  "filePath": "reports/employees_simple.csv",
  "reportType": "simple"
}
```

### Employee Endpoints

| Method | URL | Description |
|---|---|---|
| `GET` | `/api/employees` | Fetch all employees |
| `POST` | `/api/employees` | Create a new employee |

**Sample POST body:**

```json
{
  "firstName": "Alice",
  "lastName": "Walker",
  "email": "alice.walker@example.com",
  "department": "Engineering",
  "salary": 95000.00
}
```

---

## How Each Approach Works

### 1. Simple (No Extra Threads)

```
Request Thread ──► fetchEmployees() ──► writeCSV() ──► Response
```

The HTTP request thread handles everything end-to-end. Fine for low concurrency, but the thread is blocked throughout all I/O.

**Code path:** `EmployeeService.generateSimpleReport()`

---

### 2. Platform Threads

```
Request Thread ──► submit task ──► Response
                        │
               Fixed Thread Pool (5 OS threads)
                        │
                   fetchEmployees() ──► writeCSV()
```

Work is delegated to `Executors.newFixedThreadPool(5)`. Each worker is a real OS thread. Under high concurrency, requests queue up once all 5 threads are busy — consuming memory and causing latency.

**Code path:** `EmployeeService.generateReportWithPlatformThreads()`

---

### 3. Virtual Threads

```
Request Thread ──► submit task ──► Response
                        │
         Virtual-Thread-Per-Task Executor
                        │
   VT mounted on Carrier Thread
         │                      │
   fetchEmployees()          [I/O wait → VT parked, carrier freed]
         │
   writeCSV()               [I/O wait → VT parked, carrier freed]
```

Work is delegated to `Executors.newVirtualThreadPerTaskExecutor()`. The JVM creates one ultra-lightweight virtual thread per task. During any blocking I/O call the virtual thread is **parked** and the underlying carrier (OS) thread is freed to execute other virtual threads. This enables massive concurrency with a fraction of the resources.

**Code path:** `EmployeeService.generateReportWithVirtualThreads()`

**Bean definition (`VirtualThreadExecutorConfig`):**

```java
@Bean(name = "virtualThreadExecutor")
public Executor virtualThreadExecutor() {
    return Executors.newVirtualThreadPerTaskExecutor();
}
```

---

## Performance Benchmarking

Use **Apache Bench** to simulate concurrent load and observe the difference between the three strategies.

```bash
# Simple — single-threaded per request
ab -n 300 -c 100 http://localhost:8081/api/reports/simple

# Platform threads — fixed pool of 5 OS threads
ab -n 300 -c 100 http://localhost:8081/api/reports/platform

# Virtual threads — one virtual thread per task
ab -n 300 -c 100 http://localhost:8081/api/reports/virtual
```

> `-n 300` = total requests  
> `-c 100` = 100 concurrent requests at a time

Watch the application logs for thread names and timing:

```
Simple Report   - Thread: Thread[#34,http-nio-8081-exec-3,5,main]   - Time: 12 ms
Platform Thread - Thread: Thread[#45,pool-1-thread-2,5,main]        - Time: 11 ms
Virtual Thread  - Thread: VirtualThread[#52]/runnable@ForkJoinPool.. - Time: 10 ms
```

Under high concurrency you will observe that the virtual-thread endpoint maintains lower and more consistent response times because carrier threads are never left idle waiting for I/O.

---

## Key Observations

- **Thread names** in the logs visually distinguish the three models:
  - Simple / Platform → `Thread[#n, pool-x-thread-y, ...]`
  - Virtual → `VirtualThread[#n]/runnable@ForkJoinPool-...`

- **Platform threads block** their OS thread during every I/O call, so a pool of 5 threads can only serve 5 concurrent I/O operations at a time.

- **Virtual threads unmount** from the carrier thread during I/O, so thousands of concurrent I/O operations can be in-flight with only a handful of OS threads.

- Virtual threads are **not** faster for CPU-bound work; the advantage is purely in I/O-bound / high-concurrency scenarios.

- The `reports/` directory is committed to version control to ensure the output location exists at startup. Each request overwrites the corresponding CSV file.

