# Product Comparison API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://docs.docker.com/compose/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> Uma API REST moderna para comparação de produtos, construída com Spring Boot 3, programação reativa, telemetria avançada e seguindo princípios de Clean Architecture.

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Arquitetura](#-arquitetura)
- [Tecnologias](#-tecnologias)
- [Padrões e Metodologias](#-padrões-e-metodologias)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Configuração e Execução](#-configuração-e-execução)
- [API Documentation](#-api-documentation)
- [Telemetria e Monitoramento](#-telemetria-e-monitoramento)
- [Testes](#-testes)
- [Contribuição](#-contribuição)

## 🎯 Visão Geral

A **Product Comparison API** é uma aplicação Spring Boot que permite:

- **Gerenciamento de Produtos**: CRUD completo com validação
- **Comparação de Produtos**: Análise paralela e reativa de múltiplos produtos
- **Telemetria Avançada**: Métricas de negócio e técnicas com Prometheus/Grafana
- **Documentação Automática**: OpenAPI 3.0 com Swagger UI
- **Observabilidade**: Distributed tracing, logging estruturado e health checks

### Funcionalidades Principais

- ✅ **API RESTful** com versionamento (`/api/v1/`)
- ✅ **Programação Reativa** com WebFlux para operações não-bloqueantes
- ✅ **Cache Inteligente** com Spring Cache
- ✅ **Validação Robusta** com Bean Validation
- ✅ **Telemetria Completa** com métricas de negócio
- ✅ **Containerização** com Docker Compose
- ✅ **API-First Development** com OpenAPI Generator

## 🏗️ Arquitetura

### Padrão Arquitetural

**Package by Feature dentro de Package by Layer**

```
com.compareproduct.meli/
├── controller/           # Camada de Apresentação
│   ├── product/         # Feature: Produtos
│   └── compare/         # Feature: Comparações
├── service/             # Camada de Negócio
│   ├── product/
│   └── compare/
├── repository/          # Camada de Dados
├── entity/              # Entidades JPA
├── dto/                 # Data Transfer Objects
├── model/               # Modelos de Domínio
├── mapper/              # Mapeamento entre camadas
├── telemetry/           # Observabilidade
│   ├── config/
│   └── metrics/
├── exception/           # Tratamento de Exceções
└── util/                # Utilitários
```

### Princípios Arquiteturais Aplicados

#### **Clean Architecture**
- **Separação de Responsabilidades**: Cada camada tem uma responsabilidade específica
- **Inversão de Dependência**: Interfaces definem contratos entre camadas
- **Independência de Framework**: Lógica de negócio isolada

#### **Domain-Driven Design (DDD)**
- **Bounded Contexts**: Separação clara entre Product e Compare
- **Entities**: ProductEntity com identidade própria
- **Value Objects**: Rate enum, ComparisonField
- **Services**: Lógica de domínio encapsulada

#### **SOLID Principles**
- **S**ingle Responsibility: Cada classe tem uma única responsabilidade
- **O**pen/Closed: Extensível via interfaces (ProductService, CompareService)
- **L**iskov Substitution: Implementações substituíveis
- **I**nterface Segregation: Interfaces específicas e coesas
- **D**ependency Inversion: Dependência de abstrações, não implementações

## 🛠️ Tecnologias

### Core Framework
- **Java 21** - Linguagem principal com recursos modernos
- **Spring Boot 3.2.0** - Framework base com auto-configuração
- **Spring WebFlux** - Programação reativa para comparações
- **Spring Data JPA** - Persistência com Hibernate
- **Spring Cache** - Cache em memória para performance

### Banco de Dados
- **H2 Database** - Banco em memória para desenvolvimento
- **HikariCP** - Pool de conexões de alta performance

### Documentação e API
- **OpenAPI 3.0** - Especificação da API
- **Swagger UI** - Interface interativa da documentação
- **SpringDoc** - Geração automática da documentação

### Mapeamento e Validação
- **MapStruct** - Mapeamento entre DTOs/Entities com alta performance
- **Bean Validation** - Validação declarativa com anotações
- **Hibernate Validator** - Implementação da JSR-303

### Telemetria e Observabilidade
- **Micrometer** - Métricas de aplicação
- **Prometheus** - Coleta e armazenamento de métricas
- **Grafana** - Visualização de dashboards
- **Spring Actuator** - Endpoints de monitoramento
- **Zipkin** - Distributed tracing (configurado)

### Build e Deploy
- **Maven** - Gerenciamento de dependências e build
- **Docker & Docker Compose** - Containerização e orquestração
- **OpenAPI Generator** - Geração de código API-First

## 📐 Padrões e Metodologias

### Design Patterns Implementados

#### **Repository Pattern**
```java
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    // Abstração da camada de dados
}
```

#### **Service Layer Pattern**
```java
@Service
public class ProductServiceImpl implements ProductService {
    // Lógica de negócio encapsulada
}
```

#### **DTO Pattern**
```java
public record ProductRecord(
    @NotBlank String name,
    @Positive Double price
    // Transferência de dados imutável
) {}
```

#### **Mapper Pattern**
```java
@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductEntity entity);
    // Conversão entre camadas
}
```

#### **Strategy Pattern**
```java
public enum ComparisonField {
    PRICE(Product::getPrice),
    RATING(product -> product.getRating().ordinal());
    // Estratégias de comparação
}
```

### Metodologias Aplicadas

#### **API-First Development**
- Especificação OpenAPI define contratos
- Código gerado automaticamente
- Documentação sempre sincronizada

#### **Reactive Programming**
```java
public Mono<CompareResponse> compareProducts(List<Long> productIds) {
    return Flux.fromIterable(productIds)
        .flatMap(this::fetchProduct)
        .collectList()
        .map(this::buildResponse);
}
```

#### **Test-Driven Development (TDD)**
- Testes unitários com JUnit 5
- Testes de integração com TestContainers
- Cobertura de código com JaCoCo

#### **Clean Code Principles**

**Nomenclatura Significativa**
```java
public void recordSuccessfulComparison(int productCount) {
    // Nome expressa claramente a intenção
}
```

**Funções Pequenas e Focadas**
```java
private String categorizeComparisonSize(int productCount) {
    if (productCount <= 2) return "small";
    if (productCount <= 5) return "medium";
    return "large";
}
```

## 📁 Estrutura do Projeto

```
product-comparison-api/
├── src/
│   ├── main/
│   │   ├── java/com/compareproduct/meli/
│   │   │   ├── controller/
│   │   │   │   ├── product/ProductController.java
│   │   │   │   └── compare/CompareController.java
│   │   │   ├── service/
│   │   │   │   ├── product/
│   │   │   │   │   ├── ProductService.java
│   │   │   │   │   └── impl/ProductServiceImpl.java
│   │   │   │   └── compare/
│   │   │   │       ├── CompareService.java
│   │   │   │       └── impl/CompareServiceImpl.java
│   │   │   ├── repository/ProductRepository.java
│   │   │   ├── entity/ProductEntity.java
│   │   │   ├── dto/
│   │   │   │   ├── product/ProductRecord.java
│   │   │   │   └── compare/
│   │   │   │       ├── CompareRequest.java
│   │   │   │       └── CompareResponse.java
│   │   │   ├── model/Product.java
│   │   │   ├── mapper/ProductMapper.java
│   │   │   ├── enums/
│   │   │   │   ├── Rate.java
│   │   │   │   └── ComparisonField.java
│   │   │   ├── telemetry/
│   │   │   │   ├── config/TelemetryConfig.java
│   │   │   │   └── metrics/
│   │   │   │       ├── ProductMetrics.java
│   │   │   │       ├── ComparisonMetrics.java
│   │   │   │       └── BusinessMetrics.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ProductNotFoundException.java
│   │   │   │   ├── BadResourceRequestException.java
│   │   │   │   └── NoSuchResourceFoundException.java
│   │   │   └── util/MessageUtil.java
│   │   └── resources/
│   │       ├── api-spec.yml
│   │       ├── application.yml
│   │       ├── application-docker.yml
│   │       ├── messages.yml
│   │       └── data.sql
│   └── test/
│       └── java/com/compareproduct/meli/
├── monitoring/
│   ├── prometheus.yml
│   └── grafana/
│       ├── dashboards/
│       └── provisioning/
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

## 🚀 Configuração e Execução

### Pré-requisitos

- **Java 21+**
- **Maven 3.8+**
- **Docker & Docker Compose**

### Execução Local

```bash
# 1. Clone o repositório
git clone <repository-url>
cd product-comparison-api

# 2. Build da aplicação
mvn clean package -DskipTests

# 3. Executar localmente
mvn spring-boot:run

# 4. Acessar aplicação
curl http://localhost:8080/actuator/health
```

### Execução com Docker

```bash
# 1. Build e start dos containers
docker-compose up -d

# 2. Verificar status
docker-compose ps

# 3. Acessar serviços
# API: http://localhost:8080
# Grafana: http://localhost:3000 (admin/admin123)
# Prometheus: http://localhost:9090
```

### Gerar Código API-First

```bash
# Gerar interfaces a partir do OpenAPI
mvn clean generate-sources

# Verificar código gerado
ls target/generated-sources/openapi/
```

## 📚 API Documentation

### Endpoints Principais

#### Produtos
```http
POST   /api/v1/product           # Criar produto
GET    /api/v1/product/{id}      # Buscar produto
GET    /api/v1/product           # Listar produtos
DELETE /api/v1/product/{id}      # Deletar produto
```

#### Comparações
```http
POST   /api/v1/compare/products  # Comparar produtos
```

### Documentação Interativa

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs
- **Especificação**: `src/main/resources/api-spec.yml`

### Exemplos de Uso

#### Criar Produto
```bash
curl -X POST http://localhost:8080/api/v1/product \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone XYZ",
    "price": 899.99,
    "description": "Smartphone com 128GB",
    "rating": "FIVE_STARS",
    "specification": "6.1 polegadas, 128GB, 12MP",
    "url": "https://example.com/smartphone-xyz"
  }'
```

#### Comparar Produtos
```bash
curl -X POST http://localhost:8080/api/v1/compare/products \
  -H "Content-Type: application/json" \
  -d '{
    "productIds": [1, 2, 3]
  }'
```

## 📊 Telemetria e Monitoramento

### Métricas Implementadas

#### Métricas de Negócio
- `business.comparisons.successful.total` - Comparações bem-sucedidas
- `business.comparisons.failed.total` - Comparações que falharam
- `business.products.compared.total` - Total de produtos comparados
- `business.comparisons.active.current` - Comparações ativas (Gauge)

#### Métricas Técnicas
- `products.created.total` - Produtos criados
- `products.notfound.total` - Produtos não encontrados
- `products.fetch.duration` - Tempo de busca de produtos
- `comparisons.requests.total` - Total de requisições de comparação
- `comparisons.duration` - Duração das comparações

#### Métricas de Sistema
- `http.server.requests` - Requisições HTTP
- `jvm.memory.used` - Uso de memória JVM
- `jvm.gc.pause` - Pausas do Garbage Collector

### Dashboards Grafana

#### KPIs Principais
- **Taxa de Sucesso**: Percentual de comparações bem-sucedidas
- **Volume de Operações**: Produtos comparados por minuto
- **Performance**: Tempo de resposta e throughput
- **Saúde da Aplicação**: Status, memória, CPU

#### Alertas Configuráveis
- Taxa de erro > 5%
- Tempo de resposta > 2s
- Uso de memória > 80%
- API indisponível

### Endpoints de Monitoramento

```http
GET /actuator/health          # Health check
GET /actuator/metrics         # Todas as métricas
GET /actuator/prometheus      # Métricas formato Prometheus
GET /actuator/info           # Informações da aplicação
```

## 🧪 Testes

### Estratégia de Testes

#### Pirâmide de Testes
- **Testes Unitários** (70%): Lógica de negócio isolada
- **Testes de Integração** (20%): Componentes integrados
- **Testes E2E** (10%): Fluxos completos

#### Ferramentas
- **JUnit 5** - Framework de testes
- **Mockito** - Mocking de dependências
- **TestContainers** - Testes com containers
- **WebTestClient** - Testes de API reativa

### Executar Testes

```bash
# Todos os testes
mvn test

# Testes específicos
mvn test -Dtest=ProductServiceTest

# Relatório de cobertura
mvn jacoco:report
```

## 🔧 Configuração Avançada

### Profiles de Ambiente

#### Local (`application.yml`)
- Logs detalhados para desenvolvimento
- H2 Console habilitado
- Poucos endpoints Actuator expostos

#### Docker (`application-docker.yml`)
- Logs otimizados para produção
- Métricas Prometheus habilitadas
- Todos endpoints de monitoramento expostos

### Cache Strategy

```java
@Cacheable(value = "products", key = "#id")
public Product getById(Long id) {
    // Cache individual de produtos
}

@Cacheable(value = "product-comparisons", key = "#productIds.hashCode()")
public Mono<CompareResponse> compareProducts(List<Long> productIds) {
    // Cache de comparações por combinação de IDs
}
```

### Configuração de Telemetria

```yaml
management:
  metrics:
    tags:
      application: product-comparison-api
      environment: ${ENVIRONMENT:local}
    export:
      prometheus:
        enabled: true
```

## 🤝 Contribuição

### Padrões de Código

1. **Seguir Clean Code**: Nomes significativos, funções pequenas
2. **Testes Obrigatórios**: Cobertura mínima de 80%
3. **Documentação**: JavaDoc para APIs públicas
4. **Commits Semânticos**: `feat:`, `fix:`, `docs:`, `refactor:`

### Processo de Desenvolvimento

1. **Fork** do repositório
2. **Branch** para feature: `git checkout -b feature/nova-funcionalidade`
3. **Implementar** com testes
4. **Commit** com mensagem descritiva
5. **Push** e criar **Pull Request**

### Code Review Checklist

- [ ] Testes passando
- [ ] Cobertura de código adequada
- [ ] Documentação atualizada
- [ ] Métricas implementadas
- [ ] Performance considerada
- [ ] Segurança validada

## 📄 Licença

Este projeto está licenciado sob a [MIT License](LICENSE).
