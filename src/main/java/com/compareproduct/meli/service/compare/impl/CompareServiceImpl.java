package com.compareproduct.meli.service.compare.impl;

import com.compareproduct.meli.dto.compare.CompareResponse;
import com.compareproduct.meli.model.Product;
import com.compareproduct.meli.mapper.ProductMapper;
import com.compareproduct.meli.service.compare.CompareService;
import com.compareproduct.meli.service.product.ProductService;
import com.compareproduct.meli.telemetry.metrics.ComparisonMetrics;
import com.compareproduct.meli.telemetry.metrics.BusinessMetrics;
import com.compareproduct.meli.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class CompareServiceImpl implements CompareService {

    private static final Logger log = LoggerFactory.getLogger(CompareServiceImpl.class);
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ComparisonMetrics comparisonMetrics;
    private final BusinessMetrics businessMetrics;

    public CompareServiceImpl(ProductService productService, 
                            ProductMapper productMapper,
                            ComparisonMetrics comparisonMetrics,
                            BusinessMetrics businessMetrics) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.comparisonMetrics = comparisonMetrics;
        this.businessMetrics = businessMetrics;
    }

    @Override
    @Cacheable(value = "product-comparisons", key = "#productIds.hashCode()")
    public Mono<CompareResponse> compareProducts(List<Long> productIds) {

        if (productIds == null || productIds.isEmpty()) {
            log.error("COMPARISON_ERROR reason=empty_product_list productIds={}", productIds);
            businessMetrics.recordFailedComparison();
            return Mono.error(new IllegalArgumentException(
                MessageUtil.getMessage("messages.compare.productIds.empty")));
        }
        
        log.info("COMPARISON_STARTED productCount={} productIds={}", productIds.size(), productIds);
        comparisonMetrics.recordComparisonRequest();
        businessMetrics.recordActiveComparisons(1);
        
        long startTime = System.currentTimeMillis();
        
        return productService.getByIds(productIds)
                .collectList()
                .map(this::buildResponse)
                .doOnSuccess(result -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.info("COMPARISON_SUCCESS productCount={} duration={}ms productsFound={}", 
                            productIds.size(), duration, result.products().size());
                    businessMetrics.recordSuccessfulComparison(result.products().size());
                    businessMetrics.recordActiveComparisons(-1);
                    comparisonMetrics.recordComparisonDuration(duration);
                })
                .doOnError(error -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.error("COMPARISON_ERROR productCount={} duration={}ms error={}", 
                            productIds.size(), duration, error.getMessage(), error);
                    businessMetrics.recordFailedComparison();
                    businessMetrics.recordActiveComparisons(-1);
                });
    }

    private CompareResponse buildResponse(List<Product> products) {
        Map<String, List<Object>> comparison = new HashMap<>();
        comparison.put("price", products.stream().map(Product::getPrice).map(Object.class::cast).toList());
        comparison.put("rating", products.stream().map(Product::getRating).map(Object.class::cast).toList());
        comparison.put("specification", products.stream().map(Product::getSpecification).map(Object.class::cast).toList());
        
        return new CompareResponse(
            products.stream().map(productMapper::toProductSummary).toList(),
            comparison
        );
    }
}