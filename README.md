# Product Comparison API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://docs.docker.com/compose/)
[![Coverage](https://img.shields.io/badge/Coverage-100%25-brightgreen.svg)](target/site/jacoco/index.html)
[![Tests](https://img.shields.io/badge/Tests-Passing-brightgreen.svg)](TEST-STRATEGY.md)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A modern REST API for product comparison built with Spring Boot 3, reactive programming, comprehensive testing, and advanced observability.

## 📋 Table of Contents

- [Overview](#-overview)
- [Quick Start](#-quick-start)
- [API Usage](#-api-usage)
- [Architecture](#-architecture)
- [Testing Strategy](#-testing-strategy)
- [Monitoring & Observability](#-monitoring--observability)
- [Development](#-development)
- [Future Improvements](#-future-improvements)
- [Contributing](#-contributing)

## 🎯 Overview

The Product Comparison API provides a comprehensive solution for product management and comparison with enterprise-grade features:

### 🚀 Core Features
- **Product Management**: Full CRUD operations with comprehensive validation
- **Product Comparison**: Reactive parallel analysis of multiple products
- **Advanced Testing**: 100% code coverage with unit, integration, and E2E tests
- **Observability**: Complete monitoring with Prometheus, Grafana, and Allure reports
- **Documentation**: Auto-generated OpenAPI 3.0 with interactive Swagger UI

### ✨ Key Highlights
- ✅ **RESTful API** with proper versioning (`/api/v1/`)
- ✅ **Reactive Programming** with WebFlux for non-blocking operations
- ✅ **Intelligent Caching** with Spring Cache for optimal performance
- ✅ **Comprehensive Validation** with Bean Validation
- ✅ **Business & Technical Metrics** for complete observability
- ✅ **Docker Containerization** for easy deployment
- ✅ **API-First Development** with OpenAPI Generator
- ✅ **100% Test Coverage** with detailed reporting

## 🚀 Quick Start

### 📝 Prerequisites

- **Java 21+**
- **Maven 3.8+**
- **Docker & Docker Compose**

### 🐳 Run with Docker (Recommended)

```bash
# Start all services (API + Monitoring Stack)
docker-compose up -d

# Check service status
docker-compose ps

# View logs
docker-compose logs -f product-comparison-api
```

**🌐 Access Points:**
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Grafana**: http://localhost:3000 (admin/admin123)
- **Prometheus**: http://localhost:9090

### 💻 Run Locally

```bash
# Clone and build
git clone <repository-url>
cd product-comparison-api
mvn clean package -DskipTests

# Run application
mvn spring-boot:run

# Health check
curl http://localhost:8080/actuator/health
```

### 🧪 Run Tests

```bash
# Run complete test suite with coverage
./run-tests.bat  # Windows
./run-tests.sh   # Linux/Mac

# View test reports
mvn allure:serve  # Interactive Allure report
```

## 📚 API Usage

### 🔗 Endpoints

#### 📦 Products
```http
POST   /api/v1/product           # Create product
GET    /api/v1/product/{id}      # Get product by ID
GET    /api/v1/product           # List all products
DELETE /api/v1/product/{id}      # Delete product
```

#### ⚖️ Comparisons
```http
POST   /api/v1/compare/products  # Compare multiple products
```

### 📝 Examples

#### Create Product
```bash
curl -X POST http://localhost:8080/api/v1/product \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone XYZ",
    "price": 899.99,
    "description": "Latest smartphone with 128GB storage",
    "rating": "FIVE_STARS",
    "specification": "6.1 inches, 128GB, 12MP camera",
    "url": "https://example.com/smartphone-xyz"
  }'
```

#### Compare Products
```bash
curl -X POST http://localhost:8080/api/v1/compare/products \
  -H "Content-Type: application/json" \
  -d '{
    "productIds": [1, 2, 3]
  }'
```

### 📊 Response Format

#### Product Response
```json
{
  "id": 1,
  "name": "Smartphone XYZ",
  "price": 899.99,
  "description": "Latest smartphone with 128GB storage",
  "rating": "FIVE_STARS",
  "specification": "6.1 inches, 128GB, 12MP camera",
  "url": "https://example.com/smartphone-xyz"
}
```

#### Comparison Response
```json
{
  "products": [
    { /* product 1 */ },
    { /* product 2 */ },
    { /* product 3 */ }
  ],
  "totalProducts": 3,
  "comparisonSummary": "Comparison of 3 products completed successfully"
}
```

### 📚 Documentation

- **🌐 Swagger UI**: http://localhost:8080/swagger-ui.html
- **📄 OpenAPI JSON**: http://localhost:8080/api-docs
- **📜 API Specification**: `src/main/resources/api-spec.yml`

## 🏗️ Architecture

### 📁 Project Structure

```
com.compareproduct.meli/
├── 🌐 controller/           # REST Controllers
│   ├── product/         # Product endpoints
│   └── compare/         # Comparison endpoints
├── ⚙️ service/             # Business Logic
│   ├── product/
│   └── compare/
├── 💾 repository/          # Data Access Layer
├── 📦 entity/              # JPA Entities
├── 📤 dto/                 # Data Transfer Objects
├── 🏠 model/               # Domain Models
├── 🔄 mapper/              # Object Mapping (MapStruct)
├── 📊 telemetry/           # Observability
│   ├── config/
│   └── metrics/
├── ⚠️ exception/           # Exception Handling
└── 🔧 util/                # Utilities
```

### 🛠️ Technology Stack

#### **🔥 Core Technologies**
- **Java 21** - Latest LTS with modern language features
- **Spring Boot 3.2.0** - Enterprise-grade framework
- **Spring WebFlux** - Reactive programming for scalability
- **Spring Data JPA** - Data persistence with Hibernate
- **H2 Database** - In-memory database for development

#### **📊 Observability Stack**
- **Micrometer** - Application metrics collection
- **Prometheus** - Metrics storage and querying
- **Grafana** - Advanced dashboards and visualization
- **Spring Actuator** - Production-ready monitoring endpoints

#### **📚 Documentation & API**
- **OpenAPI 3.0** - API specification standard
- **Swagger UI** - Interactive API documentation
- **SpringDoc** - Automatic documentation generation

#### **🛠️ Build & Deployment**
- **Maven** - Dependency management and build automation
- **Docker & Docker Compose** - Containerization and orchestration
- **OpenAPI Generator** - Code generation from API specs

### 🎨 Design Patterns

- **🏢 Repository Pattern** - Data access abstraction
- **🎆 Service Layer Pattern** - Business logic encapsulation
- **📦 DTO Pattern** - Data transfer optimization
- **🔄 Mapper Pattern** - Object transformation with MapStruct
- **⚙️ Strategy Pattern** - Flexible comparison algorithms

## 🧪 Testing Strategy

This project implements a **comprehensive testing strategy with 100% code coverage** using modern testing practices and tools.

### 📈 Test Pyramid

```
        E2E Tests (10%)
       /              \
    Integration Tests (20%)
   /                      \
Unit Tests (70%)
```

### 🛠️ Test Categories

| **Category** | **Purpose** | **Tools** | **Coverage** |
|-------------|-------------|-----------|-------------|
| **🧩 Unit Tests** | Component isolation testing | JUnit 5, Mockito | 70% |
| **🔗 Integration Tests** | Component interaction testing | Spring Boot Test, TestContainers | 20% |
| **🌍 E2E Tests** | Complete workflow testing | REST Assured, TestContainers | 10% |

### ✨ Test Features

- **🏷️ Descriptive Annotations**: `@DisplayName`, `@Epic`, `@Feature`, `@Story`
- **📊 100% Coverage**: JaCoCo enforced coverage requirements
- **📈 Advanced Reporting**: Allure reports with BDD organization
- **🐳 Container Testing**: TestContainers for realistic integration tests
- **🚀 Automated Execution**: Scripts for complete test suite execution

### 📝 Quick Test Commands

```bash
# Run complete test suite with reports
./run-tests.bat  # Windows
./run-tests.sh   # Linux/Mac

# Run specific test types
mvn test                    # Unit tests only
mvn failsafe:integration-test  # Integration tests
mvn test -Dtest="**/*E2ETest.java"  # E2E tests

# Generate and view reports
mvn jacoco:report          # Coverage report
mvn allure:serve           # Interactive Allure report
```

### 📄 Test Documentation

For detailed testing information, see [TEST-STRATEGY.md](TEST-STRATEGY.md)

## 📊 Monitoring & Observability

## 📊 Monitoring & Observability

### 📈 Metrics Collection

#### **💼 Business Metrics**
- `business.comparisons.successful.total` - Successful product comparisons
- `business.comparisons.failed.total` - Failed comparison attempts
- `business.products.compared.total` - Total products analyzed
- `business.comparisons.active.current` - Active comparison processes

#### **⚙️ Technical Metrics**
- `products.created.total` - Products created via API
- `products.notfound.total` - Product lookup failures
- `products.fetch.duration` - Product retrieval performance
- `comparisons.requests.total` - Comparison API requests
- `comparisons.duration` - End-to-end comparison time

### 📉 Dashboards & Visualization

#### **📈 Grafana Dashboards** (http://localhost:3000)
- **🏠 Application Overview** - High-level system health
- **💼 Business KPIs** - Product and comparison metrics
- **⚙️ Technical Metrics** - Performance and system metrics
- **👥 System Health** - Infrastructure monitoring

#### **🎯 Key Performance Indicators**
- **Success Rate**: Percentage of successful comparisons
- **Throughput**: Products compared per minute
- **Response Time**: API response latency (P95, P99)
- **Error Rate**: Failed requests percentage

### 🔍 Health Checks & Endpoints

```http
GET /actuator/health          # 💚 Application health status
GET /actuator/metrics         # 📈 All available metrics
GET /actuator/prometheus      # 🔥 Prometheus format metrics
GET /actuator/info           # ℹ️ Application information
```

### 🚨 Alerting Strategy

Configured alerts for critical scenarios:
- **🔴 Error Rate > 5%** - High failure rate detection
- **🔶 Response Time > 2s** - Performance degradation
- **🟡 Memory Usage > 80%** - Resource exhaustion warning
- **⚫ Application Down** - Service availability monitoring

## 💻 Development

### 🛠️ Development Setup

```bash
# Clone repository
git clone <repository-url>
cd product-comparison-api

# Install dependencies and build
mvn clean compile

# Run in development mode
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### 🧪 Testing & Quality

```bash
# Run complete test suite
./run-tests.bat  # Windows
./run-tests.sh   # Linux/Mac

# Run specific test categories
mvn test                           # Unit tests
mvn failsafe:integration-test     # Integration tests
mvn test -Dtest="**/*E2ETest.java" # E2E tests

# Quality checks
mvn jacoco:check                   # Verify 100% coverage
mvn jacoco:report                  # Generate coverage report
```

### 📄 Configuration Profiles

| **Profile** | **Purpose** | **Database** | **Features** |
|------------|-------------|--------------|-------------|
| **local** | Development | H2 in-memory | Debug logging, H2 console |
| **docker** | Container deployment | H2 in-memory | Production logging, metrics |
| **test** | Test execution | H2 in-memory | Test-specific configurations |

### 🚀 API-First Development

```bash
# Generate code from OpenAPI specification
mvn clean generate-sources

# View generated interfaces
ls target/generated-sources/openapi/

# Update API specification
# Edit: src/main/resources/api-spec.yml
# Regenerate: mvn generate-sources
```

### 📊 Cache Strategy

- **📦 Product Cache**: `@Cacheable("products")` - Individual product caching
- **⚖️ Comparison Cache**: `@Cacheable("product-comparisons")` - Comparison result caching
- **⏰ TTL Strategy**: Time-based expiration for data consistency

### 🔧 Development Tools

- **🌐 Hot Reload**: Spring Boot DevTools for rapid development
- **📈 Metrics**: Micrometer for custom metrics
- **📝 Logging**: Structured logging with correlation IDs
- **🔍 Debugging**: Remote debugging support on port 5005

## 🚀 Future Improvements

This project was intentionally scoped to demonstrate core product comparison functionality with enterprise-grade quality standards. However, in a production scenario, the following improvements could be implemented:

### 🔹 Comparison Engine Enhancements
- **Advanced Comparison Logic**: Support for weighted comparison criteria (price vs performance vs features)
- **Structured Specifications**: Key-value pair specifications for richer comparisons
- **Similarity Scoring**: AI-powered similarity algorithms based on product attributes
- **Customizable Criteria**: User-defined comparison priorities and filters
- **Comparison History**: Track and analyze comparison patterns

### 🔹 Performance & Scalability
- **Distributed Caching**: Redis cluster for high-availability caching
- **Database Optimization**: PostgreSQL with read replicas and connection pooling
- **Query Optimization**: Advanced indexing and pagination strategies
- **Rate Limiting**: Sophisticated rate limiting with user tiers
- **Load Balancing**: Multi-instance deployment with load balancers

### 🔹 Observability Improvements
- **SLI/SLO Definition**: Define and monitor Service Level Indicators/Objectives
- **Advanced Dashboards**: Business intelligence dashboards in Grafana
- **Distributed Tracing**: OpenTelemetry integration for request tracing
- **Log Aggregation**: ELK stack for centralized log management
- **Alerting Strategies**: PagerDuty integration for critical alerts

### 🔹 Reliability & Resilience
- **Circuit Breaker Patterns**: Resilience4j for external dependency protection
- **Retry Mechanisms**: Exponential backoff with jitter
- **Bulkhead Pattern**: Resource isolation for different operations
- **Graceful Degradation**: Fallback mechanisms for service failures
- **Health Check Improvements**: Deep health checks for dependencies

### 🔹 API Design & Features
- **API Versioning**: Comprehensive versioning strategy (v1, v2) with backward compatibility
- **REST Semantics**: Improved REST design (`/products` instead of `/product`)
- **GraphQL Support**: Flexible query capabilities for frontend applications
- **Filtering & Search**: Advanced product search with Elasticsearch
- **Batch Operations**: Bulk product operations for efficiency

### 🔹 Security Enhancements
- **Authentication & Authorization**: OAuth2/JWT with role-based access control
- **Input Validation**: Advanced request sanitization and validation
- **API Security**: Rate limiting, CORS, and security headers
- **Data Encryption**: Encryption at rest and in transit
- **Security Scanning**: Automated vulnerability scanning in CI/CD

### 🔹 Testing & Quality
- **Contract Testing**: Pact for API contract verification
- **Performance Testing**: JMeter/Gatling for load testing scenarios
- **Chaos Engineering**: Chaos Monkey for resilience testing
- **Security Testing**: OWASP ZAP integration for security testing
- **Mutation Testing**: PIT testing for test quality verification

### 🔹 DevOps & Deployment
- **Advanced CI/CD**: GitHub Actions with deployment pipelines
- **Infrastructure as Code**: Terraform for AWS/Azure infrastructure
- **Container Orchestration**: Kubernetes deployment with Helm charts
- **Blue/Green Deployment**: Zero-downtime deployment strategies
- **Environment Management**: Staging, pre-production, and production environments

### 🔹 Data & Intelligence
- **Analytics Platform**: Product comparison analytics and insights
- **Recommendation Engine**: ML-based product recommendation system
- **Trend Analysis**: Historical data analysis for market trends
- **A/B Testing**: Feature flag management for controlled rollouts
- **Data Pipeline**: ETL processes for business intelligence

### 🔹 User Experience
- **Frontend Application**: React/Angular SPA for product comparison
- **Mobile API**: Mobile-optimized endpoints and responses
- **Real-time Updates**: WebSocket support for live comparisons
- **Internationalization**: Multi-language and currency support
- **Accessibility**: WCAG compliance for inclusive design
## 🤝 Contributing

### 📝 Development Process

1. **🍴 Fork** the repository
2. **🌱 Create** feature branch: `git checkout -b feature/awesome-feature`
3. **⚙️ Implement** with comprehensive tests
4. **📝 Commit** with descriptive message following [Conventional Commits](https://conventionalcommits.org/)
5. **🚀 Push** and create Pull Request

### 📄 Code Standards

- **🧠 Clean Code**: Follow SOLID principles and clean architecture
- **📊 100% Coverage**: Maintain complete test coverage (enforced by JaCoCo)
- **📚 Documentation**: JavaDoc for all public APIs
- **🏷️ Semantic Commits**: Use conventional commit format
  - `feat:` - New features
  - `fix:` - Bug fixes
  - `docs:` - Documentation updates
  - `refactor:` - Code refactoring
  - `test:` - Test improvements

### ✅ Code Review Checklist

- [ ] **🧪 Tests**: All tests passing with 100% coverage
- [ ] **📈 Coverage**: JaCoCo coverage requirements met
- [ ] **📝 Documentation**: README and code documentation updated
- [ ] **📊 Metrics**: Business and technical metrics implemented
- [ ] **⚡ Performance**: Performance impact considered and tested
- [ ] **🔒 Security**: Security implications reviewed and validated
- [ ] **🎨 Code Style**: Consistent with project conventions
- [ ] **🔗 API Design**: RESTful principles and OpenAPI spec updated

### 🛠️ Development Environment

```bash
# Setup development environment
git clone <your-fork>
cd product-comparison-api
mvn clean compile

# Run tests before committing
./run-tests.bat  # Windows
./run-tests.sh   # Linux/Mac

# Verify coverage requirements
mvn jacoco:check
```

---

## 📄 License

This project is licensed under the [MIT License](LICENSE) - see the LICENSE file for details.

---

<div align="center">

**🚀 Built with passion for quality and excellence 🚀**

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Coverage](https://img.shields.io/badge/Coverage-100%25-brightgreen.svg)](target/site/jacoco/index.html)
[![Tests](https://img.shields.io/badge/Tests-Passing-brightgreen.svg)](TEST-STRATEGY.md)

</div>