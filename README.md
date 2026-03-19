# Product Comparison API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Architecture](https://img.shields.io/badge/Architecture-Layered-blue.svg)](#-architecture--design)
[![Coverage](https://img.shields.io/badge/Coverage-100%25-brightgreen.svg)]()
[![Tests](https://img.shields.io/badge/Tests-Unit%20|%20Integration%20|%20E2E-brightgreen.svg)]()
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Enterprise-grade REST API for product management and comparison, featuring reactive programming, comprehensive observability, and production-ready AWS EC2 deployment with full CI/CD automation.

## 📋 Table of Contents

- [Executive Summary](#-executive-summary)
- [Architecture & Design](#-architecture--design)
- [Technology Stack](#-technology-stack)
- [Getting Started](#-getting-started)
- [API Reference](#-api-reference)
- [API-First Development](#-api-first-development)
- [Testing Strategy](#-testing-strategy)
- [Observability & Telemetry](#-observability--telemetry)
- [Deployment](#-deployment)
- [Development Workflow](#-development-workflow)

---

## 🎯 Executive Summary

This project demonstrates enterprise software engineering at the senior level, combining cutting-edge technologies with proven architectural patterns. It implements a complete solution for product comparison with:

- **Microservice-ready**: Layered architecture supporting independent scaling
- **Production-grade observability**: Metrics, tracing, and dashboards with Prometheus/Grafana
- **Automated testing**: 100% code coverage with unit, integration, and E2E tests
- **API-First approach**: OpenAPI 3.0 specification driving implementation
- **Complete CI/CD pipeline**: GitHub Actions with Docker, security scanning, and AWS EC2 deployment
- **Industry best practices**: SOLID principles, Clean Code, and reactive programming

---

## 🏗️ Architecture & Design

### Design Principles

The architecture adheres to **SOLID Principles** and **Clean Architecture** to ensure maintainability, extensibility, and testability:

- **Single Responsibility**: Each component has one reason to change
- **Open/Closed**: Open for extension, closed for modification
- **Liskov Substitution**: Implementations are interchangeable via interfaces
- **Interface Segregation**: Clients depend only on interfaces they use
- **Dependency Inversion**: Depend on abstractions, not concrete implementations

### Layered Architecture Pattern

```
┌──────────────────────────────────────────────────────┐
│              REST Controller Layer                    │
│     (HTTP Handling, Request/Response Mapping)        │
└─────────────────────┬────────────────────────────────┘
                      │
┌─────────────────────▼────────────────────────────────┐
│           Business Logic Service Layer                │
│  (Business Rules, Orchestration, Validation)         │
│                                                      │
│  • ProductService (CRUD + Caching Strategy)         │
│  • ComparisonService (Orchestration)                │
│  • Strategy Pattern (Pluggable Algorithms)          │
└─────────────────────┬────────────────────────────────┘
                      │
┌─────────────────────▼────────────────────────────────┐
│       Data Access Layer (Repository Pattern)         │
│  (Transaction Management, Query Optimization)       │
│                                                      │
│  • Spring Data JPA Repositories                     │
│  • Custom Query Methods                             │
│  • Transaction Boundaries                           │
└─────────────────────┬────────────────────────────────┘
                      │
              ┌───────▼────────┐
              │  H2 Database   │
              │  (In-Memory)   │
              └────────────────┘
```

### Core Design Patterns

| Pattern | Application | Benefit |
|---------|-------------|---------|
| **Repository** | Spring Data JPA abstracts data access | Database independence, testability |
| **Dependency Injection** | Spring IoC container | Loose coupling, easier testing |
| **DTO (Data Transfer Object)** | Separate API models from entities | API contract stability, data hiding |
| **Mapper (MapStruct)** | Type-safe compile-time mapping | Performance, null-safety, maintainability |
| **Strategy Pattern** | Pluggable comparison algorithms | Open/Closed principle, extensibility |
| **Cache-Aside** | Spring @Cacheable annotations | Performance optimization, eventual consistency |
| **API-First (OpenAPI)** | Specification-driven development | Contract clarity, team alignment |

### Project Package Structure

```
com.compareproduct.meli
├── controller/
│   ├── ProductController              → GET/POST /api/v1/product
│   └── ComparisonController           → POST /api/v1/compare/products
├── service/
│   ├── ProductService                 → Business logic for products
│   ├── ComparisonService              → Comparison orchestration
│   └── strategy/
│       └── ComparisonStrategy         → Algorithm interface
├── repository/
│   └── ProductRepository              → Spring Data JPA interface
├── entity/
│   └── Product                        → JPA entity (database mapping)
├── dto/
│   ├── ProductDTO                     → API request/response model
│   ├── ComparisonRequestDTO           → Comparison request payload
│   └── ComparisonResponseDTO          → Comparison result payload
├── mapper/
│   └── ProductMapper                  → MapStruct mapper (type-safe)
├── exception/
│   ├── ProductNotFoundException       → 404 handling
│   ├── InvalidComparisonException    → Business rule violations
│   └── GlobalExceptionHandler        → Centralized error handling
├── telemetry/
│   ├── config/
│   │   └── ObservabilityConfiguration → Micrometer, Prometheus config
│   └── metrics/
│       └── ComparisonMetrics          → Custom business metrics
└── util/
    └── Validation utilities            → Bean validation, custom validators
```

---

## 💻 Technology Stack

### Core Framework
- **Java 21 LTS** - Latest Java features: records, sealed classes, virtual threads (preview)
- **Spring Boot 3.4.3** - Enterprise-grade reactive web framework
- **Spring WebFlux** - Non-blocking reactive programming model
- **Spring Data JPA** - Object-relational mapping with Hibernate

### Data & Persistence
- **H2 Database** - In-memory SQL database for development/testing
- **Hibernate ORM** - JPA implementation with advanced query capabilities
- **Spring Transactions** - Declarative transaction management (@Transactional)

### API & Documentation
- **OpenAPI 3.0** - Machine-readable API specification
- **SpringDoc OpenAPI 2.8.5** - Automatic documentation generation
- **Swagger UI** - Interactive API documentation interface
- **OpenAPI Generator 7.11.0** - Code generation from specs (API-First)

### Object Mapping & Validation
- **MapStruct 1.6.3** - Compile-time code generation for type-safe mapping
- **Bean Validation (JSR-303)** - Declarative validation with @Valid

### Observability & Monitoring
- **Micrometer 1.14.x** - Metrics collection abstraction
- **Prometheus** - Time-series metrics database
- **Grafana 10.0.0** - Metrics visualization and dashboards
- **Zipkin 2.24** - Distributed tracing for request tracking
- **Spring Actuator** - Production-ready endpoints for monitoring

### Resilience & Reliability
- **Resilience4j 2.3.0** - Circuit breaker, retry, rate limiting patterns
- **Spring Cache** - Caching abstraction with TTL strategies

### Testing Framework
- **JUnit 5** - Modern testing framework with extensions
- **Mockito** - Mock object creation for unit tests
- **AssertJ** - Fluent assertion library
- **REST Assured** - HTTP/REST testing library
- **Allure Reports 2.29.1** - BDD-style test reporting
- **TestContainers** - Docker containers for integration testing
- **WireMock 3.12.0** - HTTP mocking and stubbing
- **Reactor Test** - Reactive stream testing utilities

### Build & Deployment
- **Maven 3.8+** - Build automation and dependency management
- **Docker** - Container image building
- **Docker Compose** - Local orchestration
- **Spring Boot Maven Plugin** - Uber JAR creation

### CI/CD Pipeline
- **GitHub Actions** - Automated build, test, security scan, deploy workflow
- **Trivy** - Container security scanning
- **Allure** - Test reporting with BDD organization

---

## 🚀 Getting Started

### Prerequisites

| Component | Version | Purpose |
|-----------|---------|---------|
| Java | 21+ | Runtime environment |
| Maven | 3.8+ | Build automation |
| Docker | Latest | Container runtime |
| Docker Compose | Latest | Orchestration |

### Quick Start with Docker Compose

```bash
# Clone repository
git clone <repository-url>
cd product-comparison-api

# Start all services (API + Monitoring Stack)
docker-compose up -d

# Verify all services are healthy
docker-compose ps

# View logs
docker-compose logs -f product-comparison-api
```

**Service Access Points:**

| Service | URL | Credentials |
|---------|-----|-------------|
| **API** | http://localhost:8080 | N/A |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | N/A |
| **Prometheus** | http://localhost:9090 | N/A |
| **Grafana** | http://localhost:3000 | admin / admin123 |
| **Zipkin Tracing** | http://localhost:9411 | N/A |
| **AlertManager** | http://localhost:9093 | N/A |

### Local Development Setup

```bash
# Build the project
mvn clean compile

# Run application
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Run complete test suite
mvn clean test failsafe:integration-test

# Generate test report
mvn allure:serve
```

### Health Checks

```bash
# Application health
curl http://localhost:8080/actuator/health

# Detailed health (liveness + readiness)
curl http://localhost:8080/actuator/health/liveness
curl http://localhost:8080/actuator/health/readiness

# Available metrics
curl http://localhost:8080/actuator/metrics

# Prometheus-format metrics
curl http://localhost:8080/actuator/prometheus
```

---

## 📚 API Reference

### RESTful API Conventions

- **Versioning**: URI versioning (`/api/v1/`)
- **Status Codes**: Standard HTTP semantics (200, 201, 400, 404, 500)
- **Content Type**: `application/json` for all payloads
- **Error Format**: Structured error responses with codes and messages
- **Pagination**: Cursor-based pagination for list operations

### Endpoints

#### Product Management

```http
POST   /api/v1/product              # Create a new product
GET    /api/v1/product/{id}         # Retrieve product by ID
GET    /api/v1/product              # List all products (paginated)
DELETE /api/v1/product/{id}         # Delete product
```

#### Product Comparison

```http
POST   /api/v1/compare/products     # Compare multiple products
```

### Example: Create Product

```bash
curl -X POST http://localhost:8080/api/v1/product \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone Pro Max",
    "price": 1299.99,
    "description": "Premium smartphone with advanced AI features",
    "rating": "FIVE_STARS",
    "specification": "6.7\" AMOLED, 256GB, Snapdragon 8 Gen 3",
    "url": "https://example.com/smartphone-pro-max"
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Smartphone Pro Max",
  "price": 1299.99,
  "description": "Premium smartphone with advanced AI features",
  "rating": "FIVE_STARS",
  "specification": "6.7\" AMOLED, 256GB, Snapdragon 8 Gen 3",
  "url": "https://example.com/smartphone-pro-max",
  "createdAt": "2024-03-19T10:30:00Z",
  "updatedAt": "2024-03-19T10:30:00Z"
}
```

### Example: Compare Products

```bash
curl -X POST http://localhost:8080/api/v1/compare/products \
  -H "Content-Type: application/json" \
  -d '{
    "productIds": [1, 2, 3]
  }'
```

**Response (200 OK):**
```json
{
  "products": [
    { "id": 1, "name": "Product A", "price": 899.99, ... },
    { "id": 2, "name": "Product B", "price": 1099.99, ... },
    { "id": 3, "name": "Product C", "price": 799.99, ... }
  ],
  "totalProducts": 3,
  "comparisonSummary": "Comparison of 3 products completed successfully",
  "timestamp": "2024-03-19T10:35:00Z"
}
```

### Interactive Documentation

**Swagger UI:** http://localhost:8080/swagger-ui.html

The Swagger UI provides:
- ✅ Complete endpoint documentation
- ✅ Request/response models visualization
- ✅ Try-it-out functionality for testing
- ✅ Authentication schema configuration

---

## 🔄 API-First Development

### Why API-First?

API-First development ensures:
- **Contract Clarity**: Team alignment on API behavior before implementation
- **Parallel Work**: Frontend teams can mock endpoints while backend develops
- **Type Safety**: Code generation prevents manual errors
- **Documentation**: Spec is always in sync with code

### OpenAPI Specification

```bash
# Location: src/main/resources/api/api-spec.yml

# Generate code from specification
mvn clean generate-sources

# Generated code location:
# target/generated-sources/openapi/
#   ├── com/compareproduct/meli/api/ProductApi.java
#   ├── com/compareproduct/meli/api/ComparisonApi.java
#   └── com/compareproduct/meli/api/model/ProductDTO.java
```

### Workflow

```
┌─────────────────┐
│  OpenAPI Spec   │ ← Update specification
│  (api-spec.yml) │
└────────┬────────┘
         │
         ▼
┌─────────────────────────────┐
│ mvn generate-sources        │ ← Code generation
└────────┬────────────────────┘
         │
         ▼
┌─────────────────────────────┐
│ Generated Interfaces        │ ← Type-safe contracts
│ + DTOs/Models               │
└────────┬────────────────────┘
         │
         ▼
┌─────────────────────────────┐
│ Implement Delegates         │ ← Implement generated interfaces
│ (Business Logic Layer)      │
└─────────────────────────────┘
```

### Updating API Specification

1. Edit `src/main/resources/api/api-spec.yml`
2. Add/modify endpoint, request body, or response schema
3. Run: `mvn clean generate-sources`
4. Update service layer implementation to match contract
5. Run tests to verify compatibility

---

## 🧪 Testing Strategy

### Testing Pyramid

```
                    ▲
                   /|\
                  / | \     E2E Tests (10%)
                 /  |  \    - Full workflow
                /   |   \   - External dependencies
               /    |    \  - Database + API
              /     |     \
             /______▼______\
            /         |     \   Integration Tests (20%)
           /          |      \  - Component interaction
          /           |       \ - In-memory database
         /____________▼________\
        /              |         \
       /_______________|__________\ Unit Tests (70%)
                       ▲           - Isolated components
                                   - Mocked dependencies
```

### Test Coverage

| Category | Type | Framework | Purpose |
|----------|------|-----------|---------|
| **Unit** | 70% | JUnit 5, Mockito | Component isolation, business logic |
| **Integration** | 20% | Spring Boot Test, TestContainers | Service interaction, data layer |
| **E2E** | 10% | REST Assured, TestContainers | Complete workflows, API contracts |

### Running Tests

```bash
# Run all tests with coverage
mvn clean test failsafe:integration-test

# Unit tests only
mvn test

# Integration tests only
mvn failsafe:integration-test

# Generate coverage report
mvn jacoco:report

# Generate Allure report
mvn allure:serve

# View reports
# Coverage: target/site/jacoco/index.html
# Allure: Opens in browser automatically
```

### Test Annotations & Organization

```java
@DisplayName("Product Service Tests")
@Epic("Product Management")
class ProductServiceTest {
    
    @Nested
    @Feature("Product CRUD Operations")
    class CRUDOperations {
        
        @Test
        @Story("Create valid product")
        @DisplayName("Should create product with valid input")
        void shouldCreateProduct() {
            // Arrange
            Product product = new Product(/* ... */);
            
            // Act
            Product created = productService.create(product);
            
            // Assert
            assertThat(created).isNotNull().hasFieldOrProperty("id");
        }
    }
}
```

### Test Metrics

- **Code Coverage**: 100% (enforced by JaCoCo)
- **Test Count**: 50+ unit tests, 15+ integration tests, 10+ E2E tests
- **Execution Time**: < 30 seconds
- **Reports**: Allure BDD-style reporting with detailed analytics

---

## 📊 Observability & Telemetry

### Metrics Collection Strategy

The application implements **business metrics** and **technical metrics** using Micrometer with Prometheus output.

#### Business Metrics

Track product comparison behavior and business outcomes:

```
business.comparisons.successful.total        # Counter: Successful comparisons
business.comparisons.failed.total             # Counter: Failed comparisons
business.products.compared.total              # Counter: Products analyzed
business.comparisons.active.current           # Gauge: Active comparisons
business.comparison.duration.seconds          # Timer: Comparison processing time
```

#### Technical Metrics

Monitor system performance and reliability:

```
products.created.total                        # Counter: Products via API
products.notfound.total                       # Counter: 404 errors
products.fetch.duration.seconds               # Timer: Query performance
http.requests.total                           # Counter: API requests
http.responses.size.bytes                     # Summary: Response size
jvm.memory.used.bytes                         # Gauge: JVM memory usage
jvm.threads.live                              # Gauge: Active threads
```

### Prometheus Integration

**Configuration:** `monitoring/prometheus.yml`

```yaml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'spring-boot-app'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['product-comparison-app:8080']
```

**Querying Metrics:**

```promql
# Successful comparison rate (last 5 minutes)
rate(business.comparisons.successful.total[5m])

# Average comparison duration
rate(business.comparison.duration.seconds_sum[5m]) / rate(business.comparison.duration.seconds_count[5m])

# Memory usage percentage
jvm.memory.used.bytes / jvm.memory.max.bytes * 100

# Error rate
rate(http.requests.total{status=~"5.."}[5m]) / rate(http.requests.total[5m])
```

### Grafana Dashboards

**Access:** http://localhost:3000 (admin / admin123)

Dashboards provide visualization of:

1. **Application Overview** - Health, uptime, key metrics
2. **Business KPIs** - Comparison success rate, throughput, trends
3. **Technical Metrics** - Response times (P50, P95, P99), error rates, JVM stats
4. **System Health** - Memory, CPU, thread counts, garbage collection

### Distributed Tracing (Zipkin)

**Access:** http://localhost:9411

Traces requests across the system:

- Request entry point (Controller)
- Service layer processing (Business logic)
- Data layer operations (Repository, database queries)
- External calls (if any)

Each trace includes:
- Timeline visualization
- Latency breakdown by service
- Error detection and logging
- Dependency graph

### Health Check Endpoints

```http
GET /actuator/health                          # Overall health status
GET /actuator/health/liveness                 # Container liveness probe
GET /actuator/health/readiness                # Container readiness probe
GET /actuator/info                            # Application info (version, build)
GET /actuator/metrics                         # Available metrics list
GET /actuator/prometheus                      # Metrics in Prometheus format
```

### Alert Rules (AlertManager)

**Configuration:** `monitoring/alert_rules.yml`

| Alert | Condition | Severity | Action |
|-------|-----------|----------|--------|
| High Error Rate | Errors > 5% | Critical | Page on-call |
| Slow Response | P99 > 2s | Warning | Log for analysis |
| Memory Pressure | Heap > 80% | Warning | Monitor trend |
| Application Down | No heartbeat | Critical | Page on-call |

---

## 🌍 Deployment

### Docker Container

```bash
# Build image locally
docker build -t product-comparison-api:1.0 .

# Run container
docker run -d \
  -p 8080:8080 \
  --name product-comparison-api \
  -e SPRING_PROFILES_ACTIVE=docker \
  product-comparison-api:1.0

# Access application
curl http://localhost:8080/actuator/health
```

### Docker Compose (Complete Stack)

Includes: API + Prometheus + Grafana + Zipkin + AlertManager

```bash
# Start all services
docker-compose up -d

# View status
docker-compose ps

# View logs
docker-compose logs -f product-comparison-api

# Stop services
docker-compose down
```

### AWS EC2 Deployment

The CI/CD pipeline automates deployment to AWS EC2:

**Deployment Steps:**

1. **Build & Package** - Maven builds uber JAR
2. **Security Scan** - Trivy scans Docker image for vulnerabilities
3. **Push to Docker Hub** - Image pushed with `:latest` tag
4. **SSH to EC2** - Connect via SSH with GitHub Secrets credentials
5. **Pull & Deploy** - Docker pull latest image and start container
6. **Start Monitoring** - Prometheus, Grafana, Zipkin stack initialization

**EC2 Setup Requirements:**

```bash
# On EC2 instance:
sudo yum install docker
sudo systemctl start docker
sudo usermod -aG docker ec2-user

# Docker Compose installation
sudo curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

**GitHub Secrets Configuration:**

```
DOCKERHUB_USERNAME      → Docker Hub username
DOCKERHUB_TOKEN         → Docker Hub access token
EC2_HOST                → EC2 instance public IP/hostname
EC2_USER                → EC2 SSH user (ec2-user for Amazon Linux)
EC2_SSH_KEY             → EC2 private SSH key
```

**Access Deployed Application:**

- **API**: http://{EC2_IP}:8080
- **Swagger**: http://{EC2_IP}:8080/swagger-ui.html
- **Grafana**: http://{EC2_IP}:3000

### Database Configuration

Currently uses **H2 in-memory database** for simplicity. In production:

```properties
# For PostgreSQL:
spring.datasource.url=jdbc:postgresql://prod-db:5432/products
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL13Dialect

# Connection pooling:
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
```

---

## 🛠️ Development Workflow

### Local Development

```bash
# 1. Clone and setup
git clone <repository-url>
cd product-comparison-api
mvn clean compile

# 2. Start with Docker Compose
docker-compose up -d

# 3. Run application
mvn spring-boot:run -Dspring-boot.run.profiles=local

# 4. Monitor during development
# API Docs: http://localhost:8080/swagger-ui.html
# Metrics: http://localhost:9090
# Logs: docker-compose logs -f

# 5. Run tests
mvn clean test failsafe:integration-test

# 6. View coverage
mvn jacoco:report
open target/site/jacoco/index.html

# 7. View test reports
mvn allure:serve
```

### Code Quality Checks

```bash
# Static analysis (if configured)
mvn spotbugs:check
mvn checkstyle:check

# Test coverage verification
mvn jacoco:check  # Must pass 100% coverage

# Code formatting
mvn spotless:check
mvn spotless:apply  # Auto-format
```

### Git Workflow

```bash
# 1. Create feature branch
git checkout -b feature/add-advanced-comparison

# 2. Make changes with tests
# 3. Commit with semantic message
git commit -m "feat(comparison): add advanced similarity scoring"

# Commit types:
# feat:     New feature
# fix:      Bug fix
# refactor: Code refactoring
# test:     Test additions/changes
# docs:     Documentation
# perf:     Performance improvements
# ci:       CI/CD pipeline changes

# 4. Push and create Pull Request
git push origin feature/add-advanced-comparison

# 5. CI pipeline runs:
#    - Build & test
#    - Code coverage verification
#    - Security scanning
#    - Docker image build
```

### CI/CD Pipeline (GitHub Actions)

Located in: `.github/workflows/ci_cd.yml`

**Pipeline Stages:**

1. **Build & Test** (Ubuntu Latest)
   - Checkout code
   - Setup Java 21 (Amazon Corretto)
   - Maven: Clean package
   - Runs all tests with coverage

2. **Security Scanning**
   - Docker image security scan (Trivy)
   - Detects known vulnerabilities

3. **Docker Build & Push**
   - Build Docker image
   - Login to Docker Hub
   - Push with `:latest` tag

4. **AWS EC2 Deployment**
   - SSH into EC2 instance
   - Docker login and pull latest image
   - Stop old container
   - Start new container with environment variables
   - Start Prometheus, Grafana, Zipkin stack

5. **Post-Deployment**
   - Create Pull Request to `main` branch
   - Merge after review

**Triggers:**
- Push to `developer` branch

**Artifacts:**
- Built JAR: `target/product-comparison-api-1.0.1.jar`
- Docker image: Pushed to Docker Hub

### Creating Pull Request

```markdown
# PR Title: feat: add advanced product similarity comparison

## Description
Add machine learning-based similarity scoring for product comparisons.

## Type of Change
- [x] New feature
- [ ] Bug fix
- [ ] Breaking change
- [ ] Documentation

## Testing
- [x] Unit tests added (95 lines)
- [x] Integration tests added (50 lines)
- [x] E2E tests added (30 lines)
- [x] Coverage: 100%

## Checklist
- [x] Tests passing
- [x] Coverage requirements met
- [x] Code follows style guidelines
- [x] Documentation updated
- [x] No breaking changes to API

## Related Issues
Closes #42
```

---

## 📈 Best Practices Implemented

### Code Organization
- ✅ Layered architecture with clear separation of concerns
- ✅ Package-by-feature organization
- ✅ Service layer abstraction for business logic
- ✅ Repository pattern for data access

### Testing
- ✅ 100% code coverage (enforced)
- ✅ Test pyramid: 70% unit, 20% integration, 10% E2E
- ✅ Descriptive test names with @DisplayName
- ✅ BDD-style Allure reporting
- ✅ TestContainers for realistic integration tests

### API Design
- ✅ RESTful conventions (status codes, HTTP methods)
- ✅ URI versioning (/api/v1/)
- ✅ OpenAPI 3.0 specification with code generation
- ✅ Consistent error response format
- ✅ Request/response DTOs with validation

### Observability
- ✅ Business metrics (comparison success rate, throughput)
- ✅ Technical metrics (response time, error rate, JVM)
- ✅ Distributed tracing with Zipkin
- ✅ Grafana dashboards for visualization
- ✅ Health check endpoints
- ✅ Structured logging with correlation IDs

### DevOps & Deployment
- ✅ CI/CD pipeline with GitHub Actions
- ✅ Docker containerization
- ✅ Docker Compose for local orchestration
- ✅ Security scanning (Trivy) in pipeline
- ✅ AWS EC2 deployment automation
- ✅ Blue-green ready (stop old, start new)

### Code Quality
- ✅ SOLID principles adherence
- ✅ Design patterns (Repository, Service, DTO, Mapper, Strategy)
- ✅ Clean Code practices
- ✅ MapStruct for type-safe object mapping
- ✅ Bean Validation for input validation
- ✅ Global exception handling

### Documentation
- ✅ README with architecture diagrams
- ✅ OpenAPI/Swagger interactive documentation
- ✅ JavaDoc for public APIs
- ✅ Code comments for complex logic
- ✅ Configuration profile documentation

---

## 📞 Support & Maintenance

### Common Issues

**Issue: Port 8080 already in use**
```bash
# Linux/Mac
lsof -i :8080
kill -9 <PID>

# Windows PowerShell
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

**Issue: Docker Compose services not starting**
```bash
# Check logs
docker-compose logs

# Restart services
docker-compose restart

# Full restart
docker-compose down
docker-compose up -d
```

**Issue: Tests failing with "database lock"**
```bash
# Clean and rebuild
mvn clean
rm -rf target/
mvn test
```

### Performance Tuning

**Enable application profiling:**
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-XX:+UnlockCommercialFeatures -XX:+FlightRecorder"
```

**Increase heap size (if needed):**
```bash
export JAVA_OPTS="-Xmx2g -Xms1g"
mvn spring-boot:run
```

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## 🎓 Learning Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring WebFlux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
- [OpenAPI Specification](https://spec.openapis.org/oas/v3.0.3)
- [Prometheus Documentation](https://prometheus.io/docs/)
- [Grafana Documentation](https://grafana.com/docs/)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)

---

<div align="center">

**Built with excellence and enterprise-grade practices**

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://docs.docker.com/compose/)
[![Tests](https://img.shields.io/badge/Coverage-100%25-brightgreen.svg)]()

</div>

