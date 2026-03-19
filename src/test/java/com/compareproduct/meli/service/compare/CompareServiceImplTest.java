package com.compareproduct.meli.service.compare;

import com.compareproduct.meli.dto.compare.CompareResponse;
import com.compareproduct.meli.enums.Rate;
import com.compareproduct.meli.factory.TestDataFactory;
import com.compareproduct.meli.mapper.ProductMapper;
import com.compareproduct.meli.model.Product;
import com.compareproduct.meli.service.compare.impl.CompareServiceImpl;
import com.compareproduct.meli.service.product.ProductService;
import com.compareproduct.meli.telemetry.metrics.BusinessMetrics;
import com.compareproduct.meli.telemetry.metrics.ComparisonMetrics;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@Epic("Product Comparison")
@Feature("Compare Service")
@DisplayName("CompareServiceImpl Unit Tests")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CompareServiceImplTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private BusinessMetrics businessMetrics;

    @Mock
    private ComparisonMetrics comparisonMetrics;

    @InjectMocks
    private CompareServiceImpl compareService;

    @BeforeEach
    void setUp() {
        // Setup ProductMapper mock - only when needed
        lenient().when(productMapper.toProductSummary(any(Product.class)))
            .thenAnswer(invocation -> {
                Product product = invocation.getArgument(0);
                return new CompareResponse.ProductSummary(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getRating(),
                    product.getSpecification(),
                    product.getUrl()
                );
            });
    }

    @Test
    @Story("Compare Valid Products")
    @DisplayName("Should compare products successfully when valid product IDs are provided")
    @Description("Test that verifies successful product comparison with valid product IDs")
    @Severity(SeverityLevel.CRITICAL)
    void shouldCompareProductsSuccessfully() {
        // Given
        List<Long> productIds = List.of(1L, 2L, 3L);
        List<Product> products = List.of(
            TestDataFactory.createProductWithId(1L),
            TestDataFactory.createProductWithId(2L),
            TestDataFactory.createProductWithId(3L)
        );

        when(productService.getByIds(productIds)).thenReturn(Flux.fromIterable(products));

        // When
        Mono<CompareResponse> result = compareService.compareProducts(productIds);

        // Then
        StepVerifier.create(result)
            .expectNextMatches(response -> {
                boolean hasCorrectProductCount = response.products().size() == 3;
                boolean hasComparison = response.comparison() != null;
                boolean hasPriceComparison = response.comparison().containsKey("price");
                boolean hasRatingComparison = response.comparison().containsKey("rating");
                boolean hasSpecificationComparison = response.comparison().containsKey("specification");
                
                return hasCorrectProductCount && hasComparison && 
                       hasPriceComparison && hasRatingComparison && hasSpecificationComparison;
            })
            .verifyComplete();

        verify(productService).getByIds(productIds);
    }

    @Test
    @Story("Compare Products - Empty List")
    @DisplayName("Should throw IllegalArgumentException when empty product IDs are provided")
    @Description("Test that verifies IllegalArgumentException is thrown for empty product IDs list")
    @Severity(SeverityLevel.NORMAL)
    void shouldThrowExceptionWhenEmptyProductIds() {
        // Given
        List<Long> emptyIds = List.of();

        // When
        Mono<CompareResponse> result = compareService.compareProducts(emptyIds);

        // Then
        StepVerifier.create(result)
            .expectError(IllegalArgumentException.class)
            .verify();
    }

    @Test
    @Story("Compare Products - Null List")
    @DisplayName("Should throw IllegalArgumentException when null product IDs are provided")
    @Description("Test that verifies IllegalArgumentException is thrown for null product IDs")
    @Severity(SeverityLevel.NORMAL)
    void shouldThrowExceptionWhenNullProductIds() {
        // Given
        List<Long> nullIds = null;

        // When
        Mono<CompareResponse> result = compareService.compareProducts(nullIds);

        // Then
        StepVerifier.create(result)
            .expectError(IllegalArgumentException.class)
            .verify();
    }

    @Test
    @Story("Compare Products - Two Products")
    @DisplayName("Should compare two products successfully")
    @Description("Test that verifies successful comparison with minimum number of products")
    @Severity(SeverityLevel.NORMAL)
    void shouldCompareTwoProductsSuccessfully() {
        // Given
        List<Long> twoIds = List.of(1L, 2L);
        List<Product> products = List.of(
            TestDataFactory.createProductWithId(1L),
            TestDataFactory.createProductWithId(2L)
        );

        when(productService.getByIds(twoIds)).thenReturn(Flux.fromIterable(products));

        // When
        Mono<CompareResponse> result = compareService.compareProducts(twoIds);

        // Then
        StepVerifier.create(result)
            .expectNextMatches(response -> {
                boolean hasCorrectProductCount = response.products().size() == 2;
                boolean hasComparison = response.comparison() != null;
                boolean hasExpectedKeys = response.comparison().containsKey("price") &&
                                        response.comparison().containsKey("rating") &&
                                        response.comparison().containsKey("specification");
                
                return hasCorrectProductCount && hasComparison && hasExpectedKeys;
            })
            .verifyComplete();

        verify(productService).getByIds(twoIds);
    }

    @Test
    @Story("Compare Products - Service Error")
    @DisplayName("Should handle error when product service fails")
    @Description("Test that verifies error handling when product service throws exception")
    @Severity(SeverityLevel.NORMAL)
    void shouldHandleErrorWhenProductServiceFails() {
        // Given
        List<Long> productIds = List.of(1L, 2L, 3L);

        when(productService.getByIds(anyList())).thenReturn(Flux.error(new RuntimeException("Service error")));

        // When
        Mono<CompareResponse> result = compareService.compareProducts(productIds);

        // Then
        StepVerifier.create(result)
            .expectError(RuntimeException.class)
            .verify();

        verify(productService).getByIds(productIds);
    }

    @Test
    @Story("Compare Products - No Products Found")
    @DisplayName("Should handle case when no products are found")
    @Description("Test that verifies handling when no products are found for given IDs")
    @Severity(SeverityLevel.NORMAL)
    void shouldHandleCaseWhenNoProductsFound() {
        // Given
        List<Long> productIds = List.of(999L, 998L, 997L);

        when(productService.getByIds(productIds)).thenReturn(Flux.empty());

        // When
        Mono<CompareResponse> result = compareService.compareProducts(productIds);

        // Then
        StepVerifier.create(result)
            .expectNextMatches(response -> {
                boolean hasNoProducts = response.products().isEmpty();
                boolean hasComparison = response.comparison() != null;
                boolean hasExpectedKeys = response.comparison().containsKey("price") &&
                                        response.comparison().containsKey("rating") &&
                                        response.comparison().containsKey("specification");
                
                return hasNoProducts && hasComparison && hasExpectedKeys;
            })
            .verifyComplete();

        verify(productService).getByIds(productIds);
    }

    @Test
    @Story("Compare Products - Single Product")
    @DisplayName("Should compare single product successfully")
    @Description("Test that verifies successful comparison with single product ID")
    @Severity(SeverityLevel.NORMAL)
    void shouldCompareSingleProductSuccessfully() {
        // Given
        List<Long> singleId = List.of(1L);
        Product product = TestDataFactory.createProductWithId(1L);

        when(productService.getByIds(singleId)).thenReturn(Flux.just(product));

        // When
        Mono<CompareResponse> result = compareService.compareProducts(singleId);

        // Then
        StepVerifier.create(result)
            .expectNextMatches(response -> {
                boolean hasOneProduct = response.products().size() == 1;
                boolean hasComparison = response.comparison() != null;
                boolean hasExpectedKeys = response.comparison().containsKey("price") &&
                                        response.comparison().containsKey("rating") &&
                                        response.comparison().containsKey("specification");
                
                return hasOneProduct && hasComparison && hasExpectedKeys;
            })
            .verifyComplete();

        verify(productService).getByIds(singleId);
    }
}