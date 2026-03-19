# Product Comparison API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Architecture](https://img.shields.io/badge/Architecture-Layered-blue.svg)](#-architecture)
[![Coverage](https://img.shields.io/badge/Coverage-100%25-brightgreen.svg)]()
[![Tests](https://img.shields.io/badge/Tests-Automated-brightgreen.svg)]()
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Enterprise REST API for product management and comparison. Built for production with reactive programming, complete observability (Prometheus/Grafana), 100% test coverage, and AWS EC2 deployment automation via GitHub Actions CI/CD.

## Quick Navigation

- [Design Thinking](#design-thinking) - Architecture priorities and decisions
- [Architecture](#architecture) - Layered design with SOLID principles
- [Getting Started](#getting-started) - Docker Compose quick start
- [API Reference](#api-reference) - Endpoints and examples
- [Observability](#observability) - Metrics, tracing, dashboards
- [Testing](#testing) - Test strategy and coverage
- [CI/CD & Deployment](#cicd--deployment) - Full automation pipeline

---

## Design Thinking

This project prioritizes **production-grade reliability** over feature quantity. Design decisions reflect three core principles:

### 🎯 1. Observability First
**Why it matters**: You can't operate what you can't measure.

- **Custom Business Metrics**: Track comparisons (success/failure rate, throughput)
- **Technical Metrics**: Monitor system health (response time, error rate, JVM)
- **Distributed Tracing**: Understand request flow across layers with Zipkin
- **Real-time Dashboards**: Grafana visualizations inform operational decisions

**Trade-off**: Additional infrastructure (Prometheus, Grafana) pays for itself in reduced MTTR.

### 🔒 2. Reliability Over Complexity
**Why it matters**: A simple system that works beats a complex one that breaks.

- **100% Test Coverage**: Enforced by JaCoCo - no untested code paths
- **Layered Architecture**: Clear separation enables independent component testing
- **Global Exception Handling**: Consistent, predictable error responses
- **Health Checks**: Liveness/readiness probes for orchestration platforms

**Trade-off**: More code initially, but refactoring is safe and fearless.

### ⚡ 3. Scalability-Ready Architecture
**Why it matters**: Plan for growth without major rewrites.

- **Spring WebFlux**: Non-blocking I/O handles 10x+ concurrent requests with fewer threads
- **Reactive Streams**: Backpressure prevents resource exhaustion
- **Stateless Services**: Horizontal scaling ready
- **Cache Strategy**: Reduces DB load; easy to upgrade to Redis

**Trade-off**: Learning curve for reactive programming; benefits compound at scale.

### 📋 Decision Log

| Decision | Rationale | Impact |
|----------|-----------|--------|
| **API-First (OpenAPI 3.0)** | Spec drives code; contracts clear before implementation | Frontend/backend work independently |
| **Spring Data JPA** | Type-safe queries; familiar to most Java developers | Better than string-based SQL queries |
| **MapStruct** | Compile-time mapping avoids reflection performance hit | Type-safe; errors caught at build time |
| **Docker Compose** | Local environment mirrors production | Reduces "works on my machine" issues |
| **H2 In-Memory DB** | Simplifies testing; no external dependencies | Production uses PostgreSQL (config ready) |
| **Prometheus + Grafana** | Industry standard; operational visibility | Extra infrastructure but essential for production |

---


## 🏗️ System Architecture & CI/CD Flow

![System Architecture](https://i.imgur.com/z77ZwUv.png)

This diagram represents the complete lifecycle of the application:
* **CI/CD Pipeline**: Automated workflow using **GitHub Actions** for building (Maven), security scanning (**Trivy**), and pushing images to **Docker Hub**.
* **Cloud Infrastructure**: Hosted on **AWS EC2**, utilizing **Nginx** as a Reverse Proxy with SSL termination for enhanced security.
* **Containerization**: The entire stack (API, Database, Cache, and Observability) is orchestrated via **Docker Compose**.
* **Observability**: Real-time monitoring with **Prometheus** (metrics scraping) and **Grafana** (dashboards).

## Architecture

### Layered Pattern

```
┌─────────────────────────────────────────┐
│         REST API Layer                  │
│   (Controllers, Request Validation)     │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│      Business Logic Layer               │
│ (Services, Business Rules, Caching)     │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│      Data Access Layer                  │
│ (Repositories, Transactions, Queries)   │
└────────────────┬────────────────────────┘
                 │
              ┌──▼───┐
              │  DB  │
              └───────┘
```

### Why This Structure?

- **Testability**: Each layer mocked independently
- **Maintainability**: Clear responsibility boundaries
- **Scalability**: Services can be extracted to separate deployments
- **Team Collaboration**: Frontend, backend, and QA work with clear contracts

### Core Patterns Implemented

| Pattern | Use | Benefit |
|---------|-----|---------|
| **Repository** | Data access abstraction | Database swappable (H2 → PostgreSQL) |
| **DTO** | Separate API from internal models | Breaking changes don't break APIs |
| **Mapper (MapStruct)** | Type-safe object transformation | Null-safe, compile-time verified |
| **Dependency Injection** | Spring IoC container | Loose coupling, testable components |
| **Strategy** | Pluggable comparison algorithms | Add new comparison types without modifying existing code |
| **Cache-Aside** | Spring @Cacheable | Performance optimization, eventual consistency model |

---

## Getting Started

### Docker Compose (Recommended)

```bash
git clone <repo>
cd product-comparison-api
docker-compose up -d
```

**Access Points:**
- API: https://miguelprogrammer-challenge.duckdns.org
- Swagger UI: https://miguelprogrammer-challenge.duckdns.org/swagger-ui/index.html
- Prometheus: http://miguelprogrammer-challenge.duckdns.org:9090/targets
- Grafana: https://miguelprogrammer-challenge.duckdns.org/grafana
- Zipkin Traces: https://miguelprogrammer-challenge.duckdns.org:9411 - off

### Local Development

```bash
# Build and run
mvn clean compile
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Run all tests
mvn clean test failsafe:integration-test

# View coverage report
mvn jacoco:report
open target/site/jacoco/index.html

# View test report (BDD style)
mvn allure:serve
```

### Health Checks

```bash
# Application health
curl https://miguelprogrammer-challenge.duckdns.org/actuator/health

# Prometheus metrics
curl https://miguelprogrammer-challenge.duckdns.org/actuator/prometheus | head -20
```

---

## API Reference

### RESTful Conventions

- Versioning: `/api/v1/` prefix
- Status codes: Standard HTTP (200, 201, 400, 404, 500)
- Response format: `application/json`

### Endpoints

#### Products
```http
POST   /api/v1/product              Create product
GET    /api/v1/product/{id}         Get product
GET    /api/v1/product              List all products
DELETE /api/v1/product/{id}         Delete product
```

#### Comparison
```http
POST   /api/v1/compare/products     Compare multiple products
```

### Example: Create Product

```bash
curl -X POST https://miguelprogrammer-challenge.duckdns.org/api/v1/product \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Pro",
    "price": 1299.99,
    "description": "High-performance laptop",
    "rating": "FIVE_STARS",
    "specification": "16GB RAM, 512GB SSD",
    "url": "https://example.com/laptop-pro"
  }'
```

### Example: Compare Products

```bash
curl -X POST https://miguelprogrammer-challenge.duckdns.org/api/v1/compare/products \
  -H "Content-Type: application/json" \
  -d '{"productIds": [1, 2, 3]}'
```

**Response:**
```json
{
  "products": [...],
  "totalProducts": 3,
  "comparisonSummary": "Comparison completed successfully"
}
```

### Interactive Documentation

**Swagger UI**: https://miguelprogrammer-challenge.duckdns.org/swagger-ui/index.html
- Try-it-out functionality
- Request/response models
- Parameter validation details

---

## Observability

### Why It Matters

Production systems fail silently without observability. You need three pillars:

1. **Metrics** - What happened (throughput, latency, errors)
2. **Logs** - Details of what happened (context, stack traces)
3. **Traces** - How requests flow through the system

### Metrics Collected

**Business Metrics:**
```
business.comparisons.successful.total    # Successful comparisons
business.comparisons.failed.total         # Failed comparisons
business.comparison.duration.seconds      # Processing time
```

**Technical Metrics:**
```
http.requests.total                       # API requests
http.responses.size.bytes                 # Response size
jvm.memory.used.bytes                     # Memory usage
jvm.threads.live                          # Active threads
```

### Prometheus Queries (Examples)

```promql
# Success rate (last 5 minutes)
rate(business.comparisons.successful.total[5m]) / rate(business.comparisons.total[5m])

# P95 response time
histogram_quantile(0.95, rate(http.requests.duration_seconds_bucket[5m]))

# Memory pressure
(jvm.memory.used.bytes / jvm.memory.max.bytes) > 0.8
```

### Grafana Dashboards

Access: https://miguelprogrammer-challenge.duckdns.org/grafana

**Dashboards provided:**
- Application Overview - Health status, uptime
- Business KPIs - Comparison metrics
- Technical Performance - Response times, error rates, JVM stats
- System Health - Memory, CPU, threads

### Distributed Tracing

**Zipkin**: https://miguelprogrammer-challenge.duckdns.org:9411 - off

Each request is traced across:
- Controller → Service → Repository → Database
- Timeline visualization shows latency breakdown
- Errors and exceptions are flagged

---

## Testing

### Test Pyramid

```
         E2E Tests (10%)
        /              \
    Integration (20%)
   /                  \
Unit Tests (70%)
```

### What We Test

| Type | Framework | Coverage | Purpose |
|------|-----------|----------|---------|
| **Unit** | JUnit 5 + Mockito | 70% | Component isolation, business logic |
| **Integration** | Spring Boot Test + TestContainers | 20% | Service interaction, DB operations |
| **E2E** | REST Assured + TestContainers | 10% | Full workflow, API contracts |

### 100% Code Coverage

```bash
# Run with coverage
mvn clean test failsafe:integration-test

# Verify 100% coverage (enforced)
mvn jacoco:check

# Generate report
mvn jacoco:report
# View: target/site/jacoco/index.html
```

**Why 100%?**
- No untested code paths
- Refactoring is safe
- Documentation through tests

### Run Tests

```bash
# All tests
mvn clean test failsafe:integration-test

# Unit only
mvn test

# Integration only
mvn failsafe:integration-test

# Specific test class
mvn test -Dtest=ProductServiceTest
```

### BDD-Style Test Organization

```java
@DisplayName("Product Service - CRUD Operations")
@Epic("Product Management")
class ProductServiceTest {
    
    @Nested
    @Feature("Create Products")
    class CreateProductTests {
        
        @Test
        @DisplayName("Should create product with valid input")
        void shouldCreateValidProduct() {
            // Arrange, Act, Assert
        }
    }
}
```

**Reports**: Run `mvn allure:serve` for interactive BDD report

---
## 🚀 Performance & Continuous Reliability

To ensure the system meets production demands, we implemented a **Continuous Performance Testing** strategy integrated into the CI/CD pipeline:

* **Automated Load Testing**: Every deployment to the `developer` branch triggers a **JMeter** execution. We simulate a constant load of **3 concurrent virtual users per second** (equivalent to 180 RPM) targeting the complex `/api/v1/compare/products` POST endpoint.
* **High-Resolution Monitoring**: During the 30-second stress test, **Prometheus** is configured with a **2s scrape interval**. This allows us to visualize real-time performance spikes in **Grafana** with high precision, monitoring JVM heap pressure and HikariCP connection pool saturation.
* **Visual Evidence with Allure**: Test results are automatically exported to an **Allure Server**. This provides a historical record of performance regressions, showing response time percentiles (P95, P99) and assertion success rates for every build.
---

## CI/CD & Deployment

### GitHub Actions Pipeline

**Trigger**: Push to `developer` branch

**Pipeline Stages:**

1. **Build & Test** (Ubuntu Latest)
   - Checkout code
   - Setup Java 21
   - Maven: `mvn clean package`
   - All tests run with coverage

2. **Security Scan**
   - Trivy scans Docker image
   - Detects CVEs before deployment

3. **Docker Build & Push**
   - Multi-stage build (optimized image size)
   - Push to Docker Hub with `:latest` tag

4. **AWS EC2 Deployment**
   - SSH into EC2 instance
   - Docker pull latest image
   - Blue-green deployment (stop old, start new)
   - Prometheus, Grafana, Zipkin stack startup

5. **Auto PR Creation**
   - Creates pull request to `main` branch
   - Ready for production merge

### Docker Deployment

**Build locally:**
```bash
docker build -t product-comparison-app:1.0 .
docker run -p 8080:8080 product-comparison-app:1.0
```

**Multi-stage build** (optimized):
```dockerfile
FROM maven:3.9 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
COPY --from=builder /app/target/product-comparison-api-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### AWS EC2 Setup

**Prerequisites on EC2:**
```bash
sudo yum install docker docker-compose
sudo systemctl start docker
sudo usermod -aG docker ec2-user
```

**GitHub Secrets Required:**
```
DOCKERHUB_USERNAME      → Docker Hub username
DOCKERHUB_TOKEN         → Docker Hub access token
EC2_HOST                → EC2 instance public IP
EC2_USER                → SSH user (ec2-user)
EC2_SSH_KEY             → EC2 private SSH key
```

**Access Deployed App:**
- API: https://miguelprogrammer-challenge.duckdns.org
- Swagger: https://miguelprogrammer-challenge.duckdns.org/swagger-ui/index.html
- Grafana: https://miguelprogrammer-challenge.duckdns.org/grafana

---

## Development Workflow

### Making Changes

```bash
# 1. Create feature branch
git checkout -b feature/add-similarity-scoring

# 2. Make changes with tests
# - Write test first (TDD)
# - Implement feature
# - Ensure 100% coverage

# 3. Verify locally
mvn clean test failsafe:integration-test
docker-compose up -d
# Test against running services

# 4. Commit with semantic message
git commit -m "feat(comparison): add similarity scoring algorithm"

# Commit types: feat, fix, refactor, test, docs, perf, ci

# 5. Push and create PR
git push origin feature/add-similarity-scoring
# GitHub creates PR automatically when you push to developer
```

### Code Quality Checks

```bash
# Test coverage
mvn jacoco:check  # Fails if < 100%

# Code formatting (if configured)
mvn spotless:apply

# Static analysis (if configured)
mvn spotbugs:check
```

---

## Technology Stack

**Core**: Java 21 • Spring Boot 3.4.3 • Spring WebFlux • Spring Data JPA

**API**: OpenAPI 3.0 • Swagger UI • MapStruct

**Observability**: Micrometer • Prometheus • Grafana • Zipkin

**Testing**: JUnit 5 • Mockito • TestContainers • REST Assured • Allure

**DevOps**: Docker • Docker Compose • GitHub Actions • Trivy

**Database**: H2 (dev/test) • PostgreSQL ready (production)

---

## Common Questions

### Q: Why Spring WebFlux instead of Spring MVC?
**A:** Reactive handles 10x+ concurrent requests with fewer threads. At scale, thread count becomes a constraint; non-blocking I/O scales linearly.

### Q: Why 100% test coverage?
**A:** Dead code is expensive to maintain. 100% forces you to justify every line. Refactoring becomes safe when you have complete coverage.

### Q: Why API-First?
**A:** Contracts first prevent integration surprises. Frontend and backend teams work independently against the spec.

### Q: How to transition from H2 to PostgreSQL?
**A:** Change `application-prod.yml`:
```properties
spring.datasource.url=jdbc:postgresql://db:5432/products
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASS}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL13Dialect
```
Repository layer remains unchanged (abstraction works).

### Q: How to scale to multiple instances?
**A:** Application is stateless → horizontal scaling ready
1. Deploy multiple instances
2. Put load balancer in front (AWS ELB)
3. Prometheus scrapes all instances
4. Grafana aggregates metrics

---

## Troubleshooting

**Port 8080 in use:**
```bash
# Linux/Mac
lsof -i :8080 | grep LISTEN | awk '{print $2}' | xargs kill -9

# Windows PowerShell
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

**Docker Compose services not starting:**
```bash
docker-compose logs    # View detailed logs
docker-compose restart # Restart all
docker-compose down && docker-compose up -d  # Full restart
```

**Tests failing with "database lock":**
```bash
mvn clean
rm -rf target/
mvn test
```

---

## References

- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [OpenAPI 3.0 Spec](https://spec.openapis.org/oas/v3.0.3)
- [Prometheus Docs](https://prometheus.io/docs/)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [TestContainers](https://testcontainers.com)

---

## License

MIT License - see [LICENSE](LICENSE) file for details.

---

<div align="center">

**Production-grade software requires reliability, observability, and testability.**

Built with careful architectural decisions for long-term maintainability.

</div>

