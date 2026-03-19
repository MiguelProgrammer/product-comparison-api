package com.compareproduct.meli.e2e;

import com.compareproduct.meli.config.BaseIntegrationTest;
import com.compareproduct.meli.dto.compare.CompareRequest;
import com.compareproduct.meli.dto.product.ProductRecord;
import com.compareproduct.meli.enums.Rate;
import com.compareproduct.meli.factory.TestDataFactory;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Product Comparison API")
@Feature("End-to-End Tests")
@DisplayName("Product Comparison API E2E Tests")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProductComparisonApiE2ETest extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    private static Long createdProductId1;
    private static Long createdProductId2;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @Order(1)
    @Story("Create Product E2E")
    @DisplayName("Should create product successfully through complete API flow")
    @Description("End-to-end test that verifies complete product creation flow")
    @Severity(SeverityLevel.BLOCKER)
    void shouldCreateProductSuccessfully() {
        ProductRecord productRecord = new ProductRecord(
            null,
            "E2E Test Product",
            "E2E Test Description",
            199.99,
            Rate.FIVE_STARS,
            "E2E Test Specification",
            "https://e2e-test.com/product"
        );

        given()
            .contentType(ContentType.JSON)
            .body(productRecord)
        .when()
            .post("/api/v1/products")
        .then()
            .statusCode(201);
    }

    @Test
    @Order(2)
    @Story("Get All Products E2E")
    @DisplayName("Should retrieve all products")
    @Description("End-to-end test that verifies retrieval of all products")
    @Severity(SeverityLevel.NORMAL)
    void shouldRetrieveAllProducts() {
        given()
        .when()
            .get("/api/v1/products")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("$", isA(List.class))
            .body("size()", greaterThanOrEqualTo(1)); // At least the created product
    }

    @Test
    @Order(3)
    @Story("Compare Products E2E")
    @DisplayName("Should compare multiple products successfully through complete API flow")
    @Description("End-to-end test that verifies complete product comparison flow")
    @Severity(SeverityLevel.BLOCKER)
    void shouldCompareMultipleProductsSuccessfully() {
        // First create a second product to ensure we have at least 2 products
        ProductRecord secondProduct = new ProductRecord(
            null,
            "Second E2E Product",
            "Second E2E Description",
            299.99,
            Rate.FOUR_STARS,
            "Second E2E Specification",
            "https://e2e-test.com/product2"
        );

        given()
            .contentType(ContentType.JSON)
            .body(secondProduct)
        .when()
            .post("/api/v1/products")
        .then()
            .statusCode(201);

        // Now compare the two products (assuming they have IDs 1 and 2)
        CompareRequest compareRequest = new CompareRequest(List.of(1L, 2L));

        given()
            .contentType(ContentType.JSON)
            .body(compareRequest)
        .when()
            .post("/api/v1/compare/products")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("products", hasSize(2))
            .body("products[0].id", notNullValue())
            .body("products[0].name", notNullValue())
            .body("products[0].price", notNullValue())
            .body("products[1].id", notNullValue())
            .body("products[1].name", notNullValue())
            .body("products[1].price", notNullValue())
            .body("comparison", notNullValue())
            .body("comparison.price", notNullValue())
            .body("comparison.rating", notNullValue())
            .body("comparison.specification", notNullValue());
    }

    @Test
    @Order(4)
    @Story("Get Specific Product E2E")
    @DisplayName("Should retrieve specific product by ID")
    @Description("End-to-end test that verifies retrieval of specific product")
    @Severity(SeverityLevel.NORMAL)
    void shouldRetrieveSpecificProduct() {
        // Test with known product from data.sql (iPhone 15 Pro)
        given()
        .when()
            .get("/api/v1/products/{id}", 1L)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("name", notNullValue())
            .body("price", notNullValue())
            .body("description", notNullValue())
            .body("rating", notNullValue())
            .body("specification", notNullValue())
            .body("url", notNullValue());
    }

    @Test
    @Order(5)
    @Story("Compare Single Product E2E")
    @DisplayName("Should return bad request for single product comparison")
    @Description("End-to-end test that verifies validation for minimum 2 products")
    @Severity(SeverityLevel.NORMAL)
    void shouldReturnBadRequestForSingleProduct() {
        CompareRequest compareRequest = new CompareRequest(List.of(1L));

        given()
            .contentType(ContentType.JSON)
            .body(compareRequest)
        .when()
            .post("/api/v1/compare/products")
        .then()
            .statusCode(400)
            .body("message", equalTo("Validation failed"));
    }

    @Test
    @Order(6)
    @Story("Compare Non-Existing Products E2E")
    @DisplayName("Should handle comparison of non-existing products gracefully")
    @Description("End-to-end test that verifies handling of non-existing product IDs in comparison")
    @Severity(SeverityLevel.NORMAL)
    void shouldHandleComparisonOfNonExistingProducts() {
        CompareRequest compareRequest = new CompareRequest(List.of(999L, 1000L));

        given()
            .contentType(ContentType.JSON)
            .body(compareRequest)
        .when()
            .post("/api/v1/compare/products")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("products", hasSize(0))
            .body("comparison", notNullValue())
            .body("comparison.price", hasSize(0))
            .body("comparison.rating", hasSize(0))
            .body("comparison.specification", hasSize(0));
    }

    @Test
    @Order(7)
    @Story("Invalid Product Creation E2E")
    @DisplayName("Should handle invalid product creation gracefully")
    @Description("End-to-end test that verifies error handling for invalid product data")
    @Severity(SeverityLevel.NORMAL)
    void shouldHandleInvalidProductCreation() {
        ProductRecord invalidProduct = TestDataFactory.createInvalidProductRecord();

        given()
            .contentType(ContentType.JSON)
            .body(invalidProduct)
        .when()
            .post("/api/v1/products")
        .then()
            .statusCode(400)
            .body("message", equalTo("Validation failed"));
    }

    @Test
    @Order(8)
    @Story("Get Non-Existing Product E2E")
    @DisplayName("Should return 404 for non-existing product")
    @Description("End-to-end test that verifies 404 response for non-existing product")
    @Severity(SeverityLevel.NORMAL)
    void shouldReturn404ForNonExistingProduct() {
        given()
        .when()
            .get("/api/v1/products/{id}", 99999L)
        .then()
            .statusCode(404)
            .body("message", equalTo("Product not found"));
    }

    @Test
    @Order(9)
    @Story("Invalid Comparison Request E2E")
    @DisplayName("Should handle invalid comparison request gracefully")
    @Description("End-to-end test that verifies error handling for invalid comparison request")
    @Severity(SeverityLevel.NORMAL)
    void shouldHandleInvalidComparisonRequest() {
        CompareRequest emptyRequest = new CompareRequest(List.of());

        given()
            .contentType(ContentType.JSON)
            .body(emptyRequest)
        .when()
            .post("/api/v1/compare/products")
        .then()
            .statusCode(400)
            .body("message", equalTo("Validation failed"));
    }

    @Test
    @Order(10)
    @Story("Application Health Check E2E")
    @DisplayName("Should return healthy status for application health check")
    @Description("End-to-end test that verifies application health endpoint")
    @Severity(SeverityLevel.CRITICAL)
    void shouldReturnHealthyStatus() {
        given()
        .when()
            .get("/actuator/health")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("status", equalTo("UP"))
            .body("components", notNullValue());
    }

    @Test
    @Order(11)
    @Story("Metrics Endpoint E2E")
    @DisplayName("Should return metrics for monitoring")
    @Description("End-to-end test that verifies metrics endpoint availability")
    @Severity(SeverityLevel.NORMAL)
    void shouldReturnMetricsForMonitoring() {
        given()
        .when()
            .get("/actuator/metrics")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("names", notNullValue())
            .body("names", hasItem("jvm.memory.used"));
    }

    @Test
    @Order(12)
    @Story("Compare Three Products E2E")
    @DisplayName("Should compare three products successfully")
    @Description("End-to-end test that verifies comparison with three products")
    @Severity(SeverityLevel.NORMAL)
    void shouldCompareThreeProductsSuccessfully() {
        // First create a third product to ensure we have 3 products
        ProductRecord thirdProduct = new ProductRecord(
            null,
            "Third E2E Product",
            "Third E2E Description",
            399.99,
            Rate.THREE_STARS,
            "Third E2E Specification",
            "https://e2e-test.com/product3"
        );

        given()
            .contentType(ContentType.JSON)
            .body(thirdProduct)
        .when()
            .post("/api/v1/products")
        .then()
            .statusCode(201);

        // Now compare the three products (IDs 1, 2, and 3)
        CompareRequest compareRequest = new CompareRequest(List.of(1L, 2L, 3L));

        given()
            .contentType(ContentType.JSON)
            .body(compareRequest)
        .when()
            .post("/api/v1/compare/products")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("products", hasSize(3))
            .body("comparison.price", hasSize(3))
            .body("comparison.rating", hasSize(3))
            .body("comparison.specification", hasSize(3));
    }
}