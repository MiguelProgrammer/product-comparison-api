package com.compareproduct.meli.controller.compare;

import com.compareproduct.meli.dto.compare.CompareRequest;
import com.compareproduct.meli.dto.compare.CompareResponse;
import com.compareproduct.meli.enums.Rate;
import com.compareproduct.meli.service.compare.CompareService;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@Epic("Product Comparison")
@Feature("Compare Controller Reactive")
@DisplayName("CompareController Reactive Integration Tests")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CompareControllerReactiveIntegrationTest {

    @LocalServerPort
    private int port;

    @MockBean
    private CompareService compareService;

    private WebTestClient webTestClient;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + port)
            .build();
    }

    @Test
    @Story("Compare Valid Products")
    @DisplayName("Should compare products successfully when valid request is provided")
    @Description("Test that verifies successful product comparison via REST API with valid request")
    @Severity(SeverityLevel.CRITICAL)
    void shouldCompareProductsSuccessfully() {
        // Given
        CompareRequest request = new CompareRequest(List.of(1L, 2L, 3L));
        
        List<CompareResponse.ProductSummary> products = List.of(
            new CompareResponse.ProductSummary(1L, "Product 1", "Description 1", 99.99, Rate.FOUR_STARS, "Spec 1", "http://test.com/1"),
            new CompareResponse.ProductSummary(2L, "Product 2", "Description 2", 149.99, Rate.FIVE_STARS, "Spec 2", "http://test.com/2"),
            new CompareResponse.ProductSummary(3L, "Product 3", "Description 3", 79.99, Rate.THREE_STARS, "Spec 3", "http://test.com/3")
        );
        
        Map<String, List<Object>> comparison = Map.of(
            "price", List.of(99.99, 149.99, 79.99),
            "rating", List.of(Rate.FOUR_STARS, Rate.FIVE_STARS, Rate.THREE_STARS),
            "specification", List.of("Spec 1", "Spec 2", "Spec 3")
        );
        
        CompareResponse expectedResponse = new CompareResponse(products, comparison);

        when(compareService.compareProducts(anyList())).thenReturn(Mono.just(expectedResponse));

        // When & Then
        webTestClient.post()
            .uri("/api/v1/compare/products")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.products").isArray()
            .jsonPath("$.products.length()").isEqualTo(3)
            .jsonPath("$.comparison").exists();
    }

    @Test
    @Story("Compare Products with Empty Request")
    @DisplayName("Should return bad request when empty product IDs are provided")
    @Description("Test that verifies bad request response for empty product IDs list")
    @Severity(SeverityLevel.NORMAL)
    void shouldReturnBadRequestWhenEmptyProductIds() {
        // Given
        CompareRequest emptyRequest = new CompareRequest(List.of());

        // When & Then
        webTestClient.post()
            .uri("/api/v1/compare/products")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(emptyRequest)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @Story("Compare Products with Single Product")
    @DisplayName("Should return bad request when only one product ID is provided")
    @Description("Test that verifies bad request response for single product ID (minimum 2 required)")
    @Severity(SeverityLevel.NORMAL)
    void shouldReturnBadRequestWhenSingleProductId() {
        // Given
        CompareRequest singleRequest = new CompareRequest(List.of(1L));

        // When & Then
        webTestClient.post()
            .uri("/api/v1/compare/products")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(singleRequest)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @Story("Compare Valid Products - Two Products")
    @DisplayName("Should compare two products successfully")
    @Description("Test that verifies successful comparison with minimum number of products (2)")
    @Severity(SeverityLevel.NORMAL)
    void shouldCompareTwoProductsSuccessfully() {
        // Given
        CompareRequest twoProductsRequest = new CompareRequest(List.of(1L, 2L));
        
        List<CompareResponse.ProductSummary> products = List.of(
            new CompareResponse.ProductSummary(1L, "Product 1", "Description 1", 99.99, Rate.FOUR_STARS, "Spec 1", "http://test.com/1"),
            new CompareResponse.ProductSummary(2L, "Product 2", "Description 2", 149.99, Rate.FIVE_STARS, "Spec 2", "http://test.com/2")
        );
        
        Map<String, List<Object>> comparison = Map.of(
            "price", List.of(99.99, 149.99),
            "rating", List.of(Rate.FOUR_STARS, Rate.FIVE_STARS),
            "specification", List.of("Spec 1", "Spec 2")
        );
        
        CompareResponse twoProductsResponse = new CompareResponse(products, comparison);

        when(compareService.compareProducts(anyList())).thenReturn(Mono.just(twoProductsResponse));

        // When & Then
        webTestClient.post()
            .uri("/api/v1/compare/products")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(twoProductsRequest)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.products.length()").isEqualTo(2)
            .jsonPath("$.comparison").exists();
    }
}