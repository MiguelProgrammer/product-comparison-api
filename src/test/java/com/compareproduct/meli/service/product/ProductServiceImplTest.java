package com.compareproduct.meli.service.product;

import com.compareproduct.meli.entity.ProductEntity;
import com.compareproduct.meli.exception.BadResourceRequestException;
import com.compareproduct.meli.exception.NoSuchResourceFoundException;
import com.compareproduct.meli.factory.TestDataFactory;
import com.compareproduct.meli.mapper.ProductMapper;
import com.compareproduct.meli.model.Product;
import com.compareproduct.meli.repository.ProductRepository;
import com.compareproduct.meli.service.product.impl.ProductServiceImpl;
import com.compareproduct.meli.telemetry.metrics.ProductMetrics;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Epic("Product Management")
@Feature("Product Service")
@DisplayName("ProductServiceImpl Unit Tests")
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductMetrics productMetrics;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @Story("Create Valid Product")
    @DisplayName("Should create product successfully when valid data is provided")
    @Description("Test that verifies successful product creation with valid input data")
    @Severity(SeverityLevel.CRITICAL)
    void shouldCreateProductSuccessfully() {
        // Given
        Product product = TestDataFactory.createValidProduct();
        product.setId(null); // New product without ID
        ProductEntity productEntity = TestDataFactory.createValidProductEntity();
        ProductEntity savedEntity = TestDataFactory.createValidProductEntity();

        when(productMapper.toProductEntity(product)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(savedEntity);
        doNothing().when(productMetrics).incrementProductCreated();

        // When
        productService.create(product);

        // Then
        verify(productMapper).toProductEntity(product);
        verify(productRepository).save(productEntity);
        verify(productMetrics).incrementProductCreated();
    }

    @Test
    @Story("Create Product with Existing ID")
    @DisplayName("Should throw BadResourceRequestException when product with same ID already exists")
    @Description("Test that verifies BadResourceRequestException is thrown for duplicate product ID")
    @Severity(SeverityLevel.NORMAL)
    void shouldThrowExceptionWhenProductWithSameIdExists() {
        // Given
        Product product = TestDataFactory.createValidProduct();
        ProductEntity existingEntity = TestDataFactory.createValidProductEntity();

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(existingEntity));

        // When & Then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(BadResourceRequestException.class);

        verify(productRepository).findById(product.getId());
        verify(productRepository, never()).save(any());
        verifyNoInteractions(productMetrics);
    }

    @Test
    @Story("Create Product with Repository Error")
    @DisplayName("Should handle repository error during product creation")
    @Description("Test that verifies error handling when repository fails during product creation")
    @Severity(SeverityLevel.NORMAL)
    void shouldHandleRepositoryErrorDuringCreation() {
        // Given
        Product product = TestDataFactory.createValidProduct();
        product.setId(null);
        ProductEntity productEntity = TestDataFactory.createValidProductEntity();

        when(productMapper.toProductEntity(product)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Database error");

        verify(productMapper).toProductEntity(product);
        verify(productRepository).save(productEntity);
        verifyNoInteractions(productMetrics);
    }

    @Test
    @Story("Get Existing Product")
    @DisplayName("Should return product when valid ID is provided")
    @Description("Test that verifies successful product retrieval by existing ID")
    @Severity(SeverityLevel.CRITICAL)
    void shouldReturnProductWhenValidIdProvided() throws Exception {
        // Given
        Long productId = 1L;
        ProductEntity productEntity = TestDataFactory.createProductEntityWithId(productId);
        Product expectedProduct = TestDataFactory.createProductWithId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(productMapper.toProduct(productEntity)).thenReturn(expectedProduct);
        when(productMetrics.recordProductFetchTime(any(Callable.class))).thenAnswer(invocation -> {
            Callable<Product> callable = invocation.getArgument(0);
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // When
        Product result = productService.getById(productId);

        // Then
        assertThat(result).isEqualTo(expectedProduct);
        verify(productRepository).findById(productId);
        verify(productMapper).toProduct(productEntity);
        verify(productMetrics).recordProductFetchTime(any(Callable.class));
    }

    @Test
    @Story("Get Non-Existing Product")
    @DisplayName("Should throw NoSuchResourceFoundException when product does not exist")
    @Description("Test that verifies NoSuchResourceFoundException is thrown for non-existing product ID")
    @Severity(SeverityLevel.NORMAL)
    void shouldThrowExceptionWhenProductNotFound() throws Exception {
        // Given
        Long nonExistingId = 999L;

        when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        when(productMetrics.recordProductFetchTime(any(Callable.class))).thenAnswer(invocation -> {
            Callable<Product> callable = invocation.getArgument(0);
            try {
                return callable.call();
            } catch (NoSuchResourceFoundException e) {
                throw e; // Re-throw NoSuchResourceFoundException as-is
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        doNothing().when(productMetrics).incrementProductNotFound();

        // When & Then
        assertThatThrownBy(() -> productService.getById(nonExistingId))
            .isInstanceOf(NoSuchResourceFoundException.class);

        verify(productRepository).findById(nonExistingId);
        verify(productMetrics).incrementProductNotFound();
        verify(productMetrics).recordProductFetchTime(any(Callable.class));
        verifyNoInteractions(productMapper);
    }

    @Test
    @Story("Get All Products")
    @DisplayName("Should return all products when products exist")
    @Description("Test that verifies successful retrieval of all products")
    @Severity(SeverityLevel.NORMAL)
    void shouldReturnAllProductsWhenProductsExist() {
        // Given
        List<ProductEntity> productEntities = List.of(
            TestDataFactory.createProductEntityWithId(1L),
            TestDataFactory.createProductEntityWithId(2L)
        );
        List<Product> expectedProducts = List.of(
            TestDataFactory.createProductWithId(1L),
            TestDataFactory.createProductWithId(2L)
        );

        when(productRepository.findAll()).thenReturn(productEntities);
        when(productMapper.toProduct(productEntities.get(0))).thenReturn(expectedProducts.get(0));
        when(productMapper.toProduct(productEntities.get(1))).thenReturn(expectedProducts.get(1));

        // When
        List<Product> result = productService.getAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(expectedProducts);
        verify(productRepository).findAll();
        verify(productMapper, times(2)).toProduct(any(ProductEntity.class));
    }

    @Test
    @Story("Get All Products - Empty List")
    @DisplayName("Should return empty list when no products exist")
    @Description("Test that verifies empty list is returned when no products exist in database")
    @Severity(SeverityLevel.MINOR)
    void shouldReturnEmptyListWhenNoProductsExist() {
        // Given
        when(productRepository.findAll()).thenReturn(List.of());

        // When
        List<Product> result = productService.getAll();

        // Then
        assertThat(result).isEmpty();
        verify(productRepository).findAll();
        verifyNoInteractions(productMapper);
    }

    @Test
    @Story("Delete Existing Product")
    @DisplayName("Should delete product successfully when valid ID is provided")
    @Description("Test that verifies successful product deletion by existing ID")
    @Severity(SeverityLevel.CRITICAL)
    void shouldDeleteProductSuccessfullyWhenValidIdProvided() {
        // Given
        Long productId = 1L;

        doNothing().when(productRepository).deleteById(productId);

        // When
        productService.deleteById(productId);

        // Then
        verify(productRepository).deleteById(productId);
    }

    @Test
    @Story("Delete Product with Repository Error")
    @DisplayName("Should handle repository error during product deletion")
    @Description("Test that verifies error handling when repository fails during product deletion")
    @Severity(SeverityLevel.NORMAL)
    void shouldHandleRepositoryErrorDuringDeletion() {
        // Given
        Long productId = 1L;

        doThrow(new RuntimeException("Database error")).when(productRepository).deleteById(productId);

        // When & Then
        assertThatThrownBy(() -> productService.deleteById(productId))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Database error");

        verify(productRepository).deleteById(productId);
    }

    @Test
    @Story("Delete All Products")
    @DisplayName("Should delete all products successfully")
    @Description("Test that verifies successful deletion of all products")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteAllProductsSuccessfully() {
        // Given
        when(productRepository.count()).thenReturn(5L);
        doNothing().when(productRepository).deleteAllInBatch();

        // When
        productService.deleteAll();

        // Then
        verify(productRepository).count();
        verify(productRepository).deleteAllInBatch();
    }

    @Test
    @Story("Delete All Products with Repository Error")
    @DisplayName("Should handle repository error during delete all operation")
    @Description("Test that verifies error handling when repository fails during delete all operation")
    @Severity(SeverityLevel.NORMAL)
    void shouldHandleRepositoryErrorDuringDeleteAll() {
        // Given
        when(productRepository.count()).thenReturn(5L);
        doThrow(new RuntimeException("Database error")).when(productRepository).deleteAllInBatch();

        // When & Then
        assertThatThrownBy(() -> productService.deleteAll())
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Database error");

        verify(productRepository).count();
        verify(productRepository).deleteAllInBatch();
    }
}