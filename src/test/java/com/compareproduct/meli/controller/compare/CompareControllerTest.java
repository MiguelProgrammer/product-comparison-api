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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@Epic("Product Comparison")
@Feature("Compare Controller")
@DisplayName("CompareController Integration Tests")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CompareControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CompareService compareService;

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
            "prices", List.of(99.99, 149.99, 79.99),
            "ratings", List.of("FOUR_STARS", "FIVE_STARS", "THREE_STARS")
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
    @Story("Compare Products with Too Many Products")
    @DisplayName("Should return bad request when more than 10 product IDs are provided")
    @Description("Test that verifies bad request response for too many product IDs (maximum 10 allowed)")
    @Severity(SeverityLevel.NORMAL)
    void shouldReturnBadRequestWhenTooManyProductIds() {
        // Given
        List<Long> tooManyIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L);
        CompareRequest tooManyRequest = new CompareRequest(tooManyIds);

        // When & Then
        webTestClient.post()
            .uri("/api/v1/compare/products")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(tooManyRequest)
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
        
        CompareResponse twoProductsResponse = new CompareResponse(products, Map.of("prices", List.of(99.99, 149.99)));

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

    @Test
    @Story("Compare Products - Service Error")
    @DisplayName("Should handle internal server error when service fails")
    @Description("Test that verifies internal server error response when service throws unexpected exception")
    @Severity(SeverityLevel.NORMAL)
    void shouldHandleInternalServerErrorWhenServiceFails() {
        // Given
        CompareRequest request = new CompareRequest(List.of(1L, 2L, 3L));

        when(compareService.compareProducts(anyList()))
            .thenReturn(Mono.error(new RuntimeException("Internal service error")));

        // When & Then
        webTestClient.post()
            .uri("/api/v1/compare/products")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().is5xxServerError();
    }

    @Test
    @Story("Compare Products with Wrong Content Type")
    @DisplayName("Should return unsupported media type when wrong content type is provided")
    @Description("Test that verifies unsupported media type response for wrong content type")
    @Severity(SeverityLevel.MINOR)
    void shouldReturnUnsupportedMediaTypeWhenWrongContentType() {
        // When & Then
        webTestClient.post()
            .uri("/api/v1/compare/products")
            .contentType(MediaType.TEXT_PLAIN)
            .bodyValue("invalid text content")
            .exchange()
            .expectStatus().is4xxClientError();
    }
}