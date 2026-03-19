package com.compareproduct.meli.controller.product;

import com.compareproduct.meli.dto.product.ProductRecord;
import com.compareproduct.meli.exception.NoSuchResourceFoundException;
import com.compareproduct.meli.factory.TestDataFactory;
import com.compareproduct.meli.mapper.ProductMapper;
import com.compareproduct.meli.model.Product;
import com.compareproduct.meli.service.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Epic("Product Management")
@Feature("Product Controller")
@DisplayName("ProductController Unit Tests")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    @Test
    @Story("Create Valid Product")
    @DisplayName("Should create product successfully when valid data is provided")
    @Description("Test that verifies successful product creation via REST API with valid input data")
    @Severity(SeverityLevel.CRITICAL)
    void shouldCreateProductSuccessfully() throws Exception {
        // Given
        ProductRecord productRecord = TestDataFactory.createValidProductRecord();
        Product product = TestDataFactory.createValidProduct();

        when(productMapper.toProduct(any(ProductRecord.class))).thenReturn(product);
        doNothing().when(productService).create(any(Product.class));

        // When & Then
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRecord)))
            .andExpect(status().isCreated());

        verify(productMapper).toProduct(any(ProductRecord.class));
        verify(productService).create(any(Product.class));
    }

    @Test
    @Story("Create Product with Invalid Data")
    @DisplayName("Should return bad request when invalid data is provided")
    @Description("Test that verifies bad request response for invalid product data")
    @Severity(SeverityLevel.NORMAL)
    void shouldReturnBadRequestWhenInvalidDataProvided() throws Exception {
        // Given
        ProductRecord invalidProduct = TestDataFactory.createInvalidProductRecord();

        // When & Then
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidProduct)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Story("Create Product with Empty Body")
    @DisplayName("Should return bad request when empty body is provided")
    @Description("Test that verifies bad request response for empty request body")
    @Severity(SeverityLevel.NORMAL)
    void shouldReturnBadRequestWhenEmptyBodyProvided() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Story("Get Existing Product")
    @DisplayName("Should return product when valid ID is provided")
    @Description("Test that verifies successful product retrieval by existing ID via REST API")
    @Severity(SeverityLevel.CRITICAL)
    void shouldReturnProductWhenValidIdProvided() throws Exception {
        // Given
        Long productId = 1L;
        Product product = TestDataFactory.createProductWithId(productId);
        ProductRecord expectedProductRecord = TestDataFactory.createValidProductRecord();

        when(productService.getById(productId)).thenReturn(product);
        when(productMapper.toProductRecord(product)).thenReturn(expectedProductRecord);

        // When & Then
        mockMvc.perform(get("/api/v1/products/{id}", productId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value(expectedProductRecord.name()))
            .andExpect(jsonPath("$.price").value(expectedProductRecord.price()))
            .andExpect(jsonPath("$.description").value(expectedProductRecord.description()));

        verify(productService).getById(productId);
        verify(productMapper).toProductRecord(product);
    }

    @Test
    @Story("Get Non-Existing Product")
    @DisplayName("Should return not found when product does not exist")
    @Description("Test that verifies not found response for non-existing product ID")
    @Severity(SeverityLevel.NORMAL)
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        // Given
        Long nonExistingId = 999L;

        when(productService.getById(nonExistingId))
            .thenThrow(new NoSuchResourceFoundException("Product not found with id: " + nonExistingId));

        // When & Then
        mockMvc.perform(get("/api/v1/products/{id}", nonExistingId))
            .andExpect(status().isNotFound());

        verify(productService).getById(nonExistingId);
    }

    @Test
    @Story("Get Product with Invalid ID Format")
    @DisplayName("Should return bad request when invalid ID format is provided")
    @Description("Test that verifies bad request response for invalid ID format")
    @Severity(SeverityLevel.MINOR)
    void shouldReturnBadRequestWhenInvalidIdFormatProvided() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/products/{id}", "invalid-id"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Story("Get All Products")
    @DisplayName("Should return all products when products exist")
    @Description("Test that verifies successful retrieval of all products via REST API")
    @Severity(SeverityLevel.NORMAL)
    void shouldReturnAllProductsWhenProductsExist() throws Exception {
        // Given
        List<Product> products = List.of(
            TestDataFactory.createProductWithId(1L),
            TestDataFactory.createProductWithId(2L)
        );
        List<ProductRecord> expectedProductRecords = List.of(
            TestDataFactory.createValidProductRecord(),
            TestDataFactory.createValidProductRecord()
        );

        when(productService.getAll()).thenReturn(products);
        when(productMapper.toProductRecord(products.get(0))).thenReturn(expectedProductRecords.get(0));
        when(productMapper.toProductRecord(products.get(1))).thenReturn(expectedProductRecords.get(1));

        // When & Then
        mockMvc.perform(get("/api/v1/products"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2));

        verify(productService).getAll();
        verify(productMapper, times(2)).toProductRecord(any(Product.class));
    }

    @Test
    @Story("Get All Products - Empty List")
    @DisplayName("Should return empty array when no products exist")
    @Description("Test that verifies empty array response when no products exist")
    @Severity(SeverityLevel.MINOR)
    void shouldReturnEmptyArrayWhenNoProductsExist() throws Exception {
        // Given
        when(productService.getAll()).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/api/v1/products"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(0));

        verify(productService).getAll();
    }

    @Test
    @Story("Delete Existing Product")
    @DisplayName("Should delete product successfully when valid ID is provided")
    @Description("Test that verifies successful product deletion by existing ID via REST API")
    @Severity(SeverityLevel.CRITICAL)
    void shouldDeleteProductSuccessfullyWhenValidIdProvided() throws Exception {
        // Given
        Long productId = 1L;

        doNothing().when(productService).deleteById(productId);

        // When & Then
        mockMvc.perform(delete("/api/v1/products/{id}", productId))
            .andExpect(status().isNoContent());

        verify(productService).deleteById(productId);
    }

    @Test
    @Story("Delete Non-Existing Product")
    @DisplayName("Should return not found when trying to delete non-existing product")
    @Description("Test that verifies not found response when trying to delete non-existing product")
    @Severity(SeverityLevel.NORMAL)
    void shouldReturnNotFoundWhenTryingToDeleteNonExistingProduct() throws Exception {
        // Given
        Long nonExistingId = 999L;

        doThrow(new NoSuchResourceFoundException("Product not found with id: " + nonExistingId))
            .when(productService).deleteById(nonExistingId);

        // When & Then
        mockMvc.perform(delete("/api/v1/products/{id}", nonExistingId))
            .andExpect(status().isNotFound());

        verify(productService).deleteById(nonExistingId);
    }

    @Test
    @Story("Delete Product with Invalid ID Format")
    @DisplayName("Should return bad request when invalid ID format is provided for deletion")
    @Description("Test that verifies bad request response for invalid ID format during deletion")
    @Severity(SeverityLevel.MINOR)
    void shouldReturnBadRequestWhenInvalidIdFormatProvidedForDeletion() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/products/{id}", "invalid-id"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Story("Delete All Products")
    @DisplayName("Should delete all products successfully")
    @Description("Test that verifies successful deletion of all products")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteAllProductsSuccessfully() throws Exception {
        // Given
        doNothing().when(productService).deleteAll();

        // When & Then
        mockMvc.perform(delete("/api/v1/products"))
            .andExpect(status().isNoContent());

        verify(productService).deleteAll();
    }
}