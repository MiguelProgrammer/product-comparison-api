package com.compareproduct.meli.controller.compare;

import com.compareproduct.meli.dto.compare.CompareRequest;
import com.compareproduct.meli.dto.compare.CompareResponse;
import com.compareproduct.meli.enums.Rate;
import com.compareproduct.meli.service.compare.CompareService;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@Epic("Product Comparison")
@Feature("Compare Controller Unit")
@DisplayName("CompareController Unit Tests")
@ExtendWith(MockitoExtension.class)
class CompareControllerUnitTest {

    @Mock
    private CompareService compareService;

    @InjectMocks
    private CompareController compareController;

    @Test
    @Story("Compare Valid Products")
    @DisplayName("Should compare products successfully when valid request is provided")
    @Description("Test that verifies successful product comparison with valid request")
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

        when(compareService.compareProducts(request.productIds())).thenReturn(Mono.just(expectedResponse));

        // When
        Mono<CompareResponse> result = compareController.compareProducts(request);

        // Then
        StepVerifier.create(result)
            .expectNextMatches(response -> 
                response.products().size() == 3 &&
                response.comparison() != null &&
                response.comparison().containsKey("price") &&
                response.comparison().containsKey("rating") &&
                response.comparison().containsKey("specification")
            )
            .verifyComplete();
    }

    @Test
    @Story("Compare Products - Two Products")
    @DisplayName("Should compare two products successfully")
    @Description("Test that verifies successful comparison with minimum number of products (2)")
    @Severity(SeverityLevel.NORMAL)
    void shouldCompareTwoProductsSuccessfully() {
        // Given
        CompareRequest request = new CompareRequest(List.of(1L, 2L));
        
        List<CompareResponse.ProductSummary> products = List.of(
            new CompareResponse.ProductSummary(1L, "Product 1", "Description 1", 99.99, Rate.FOUR_STARS, "Spec 1", "http://test.com/1"),
            new CompareResponse.ProductSummary(2L, "Product 2", "Description 2", 149.99, Rate.FIVE_STARS, "Spec 2", "http://test.com/2")
        );
        
        Map<String, List<Object>> comparison = Map.of(
            "price", List.of(99.99, 149.99),
            "rating", List.of(Rate.FOUR_STARS, Rate.FIVE_STARS),
            "specification", List.of("Spec 1", "Spec 2")
        );
        
        CompareResponse expectedResponse = new CompareResponse(products, comparison);

        when(compareService.compareProducts(request.productIds())).thenReturn(Mono.just(expectedResponse));

        // When
        Mono<CompareResponse> result = compareController.compareProducts(request);

        // Then
        StepVerifier.create(result)
            .expectNextMatches(response -> 
                response.products().size() == 2 &&
                response.comparison() != null
            )
            .verifyComplete();
    }

    @Test
    @Story("Compare Products - Service Error")
    @DisplayName("Should handle error when service fails")
    @Description("Test that verifies error handling when service throws exception")
    @Severity(SeverityLevel.NORMAL)
    void shouldHandleErrorWhenServiceFails() {
        // Given
        CompareRequest request = new CompareRequest(List.of(1L, 2L, 3L));

        when(compareService.compareProducts(request.productIds()))
            .thenReturn(Mono.error(new RuntimeException("Internal service error")));

        // When
        Mono<CompareResponse> result = compareController.compareProducts(request);

        // Then
        StepVerifier.create(result)
            .expectError(RuntimeException.class)
            .verify();
    }
}