package com.compareproduct.meli.factory;

import com.compareproduct.meli.dto.compare.CompareRequest;
import com.compareproduct.meli.dto.compare.CompareResponse;
import com.compareproduct.meli.dto.product.ProductRecord;
import com.compareproduct.meli.entity.ProductEntity;
import com.compareproduct.meli.enums.Rate;
import com.compareproduct.meli.model.Product;

import java.util.List;
import java.util.Map;

/**
 * Factory class for creating test data objects
 */
public class TestDataFactory {

    public static ProductRecord createValidProductRecord() {
        return new ProductRecord(
            null,
            "Test Product",
            "Test Description",
            99.99,
            Rate.FOUR_STARS,
            "Test Specification",
            "https://test.com/product"
        );
    }

    public static ProductRecord createProductRecordWithName(String name) {
        return new ProductRecord(
            null,
            name,
            "Test Description",
            99.99,
            Rate.FOUR_STARS,
            "Test Specification",
            "https://test.com/product"
        );
    }

    public static ProductRecord createProductRecordWithPrice(Double price) {
        return new ProductRecord(
            null,
            "Test Product",
            "Test Description",
            price,
            Rate.FOUR_STARS,
            "Test Specification",
            "https://test.com/product"
        );
    }

    public static ProductRecord createInvalidProductRecord() {
        return new ProductRecord(
            null,
            "",
            "",
            -1.0,
            null,
            "",
            "invalid-url"
        );
    }

    public static ProductEntity createValidProductEntity() {
        ProductEntity entity = new ProductEntity();
        entity.setId(1L);
        entity.setName("Test Product");
        entity.setPrice(99.99);
        entity.setDescription("Test Description");
        entity.setRating(Rate.FOUR_STARS);
        entity.setSpecification("Test Specification");
        entity.setUrl("https://test.com/product");
        return entity;
    }

    public static ProductEntity createProductEntityWithId(Long id) {
        ProductEntity entity = createValidProductEntity();
        entity.setId(id);
        return entity;
    }

    public static Product createValidProduct() {
        return Product.builder()
            .id(1L)
            .name("Test Product")
            .price(99.99)
            .description("Test Description")
            .rating(Rate.FOUR_STARS)
            .specification("Test Specification")
            .url("https://test.com/product")
            .build();
    }

    public static Product createProductWithId(Long id) {
        return Product.builder()
            .id(id)
            .name("Test Product " + id)
            .price(99.99 + id)
            .description("Test Description " + id)
            .rating(Rate.FOUR_STARS)
            .specification("Test Specification " + id)
            .url("https://test.com/product/" + id)
            .build();
    }

    public static CompareRequest createValidCompareRequest() {
        return new CompareRequest(List.of(1L, 2L, 3L));
    }

    public static CompareRequest createCompareRequestWithIds(List<Long> ids) {
        return new CompareRequest(ids);
    }

    public static CompareResponse createValidCompareResponse() {
        List<CompareResponse.ProductSummary> products = List.of(
            new CompareResponse.ProductSummary(1L, "Test Product 1", "Test Description 1", 100.99, Rate.FOUR_STARS, "Test Specification 1", "https://test.com/product/1"),
            new CompareResponse.ProductSummary(2L, "Test Product 2", "Test Description 2", 101.99, Rate.FOUR_STARS, "Test Specification 2", "https://test.com/product/2"),
            new CompareResponse.ProductSummary(3L, "Test Product 3", "Test Description 3", 102.99, Rate.FOUR_STARS, "Test Specification 3", "https://test.com/product/3")
        );
        
        Map<String, List<Object>> comparison = Map.of(
            "prices", List.of(100.99, 101.99, 102.99),
            "ratings", List.of("FOUR_STARS", "FOUR_STARS", "FOUR_STARS")
        );
        
        return new CompareResponse(products, comparison);
    }
}