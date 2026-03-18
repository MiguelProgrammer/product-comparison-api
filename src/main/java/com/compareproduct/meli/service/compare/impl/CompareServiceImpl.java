package com.compareproduct.meli.service.compare.impl;

import com.compareproduct.meli.dto.compare.CompareResponse;
import com.compareproduct.meli.enums.ComparisonField;
import com.compareproduct.meli.exception.ProductNotFoundException;
import com.compareproduct.meli.model.Product;
import com.compareproduct.meli.mapper.ProductMapper;
import com.compareproduct.meli.service.compare.CompareService;
import com.compareproduct.meli.service.product.ProductService;
import com.compareproduct.meli.telemetry.metrics.ComparisonMetrics;
import com.compareproduct.meli.telemetry.metrics.BusinessMetrics;
import com.compareproduct.meli.util.MessageUtil;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

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
    @NewSpan("compare-products")
    public Mono<CompareResponse> compareProducts(@SpanTag("productIds") List<Long> productIds) throws Exception {
        if (productIds == null || productIds.isEmpty()) {
            return Mono.error(new IllegalArgumentException(MessageUtil.getMessage("messages.compare.productIds.empty")));
        }
        
        comparisonMetrics.incrementComparisonRequest();
        businessMetrics.incrementActiveComparisons();
        log.debug(MessageUtil.getMessage("messages.compare.operation.started"));
        
        return comparisonMetrics.recordComparisonTime(() -> {
            return Flux.fromIterable(productIds)
                    .flatMap(this::fetchProduct)
                    .timeout(Duration.ofSeconds(10))
                    .collectList()
                    .map(this::buildResponse)
                    .doOnSuccess(result -> {
                        businessMetrics.recordSuccessfulComparison(productIds.size());
                        businessMetrics.decrementActiveComparisons();
                        log.info(MessageUtil.getMessage("messages.compare.operation.completed"));
                    })
                    .doOnError(error -> {
                        businessMetrics.recordFailedComparison();
                        businessMetrics.decrementActiveComparisons();
                        log.error(MessageUtil.getMessage("messages.compare.operation.error"));
                    });
        });
    }

    @NewSpan("fetch-product")
    private Mono<Product> fetchProduct(@SpanTag("productId") Long id) {
        return Mono.fromCallable(() -> productService.getById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorMap(ex -> new ProductNotFoundException(MessageUtil.getMessage("messages.exception.product.notfound")));
    }

    private CompareResponse buildResponse(List<Product> products) {
        return new CompareResponse(
            products.stream().map(productMapper::toProductSummary).toList(),
            buildComparison(products)
        );
    }

    private Map<String, List<Object>> buildComparison(List<Product> products) {
        return Arrays.stream(ComparisonField.values())
                .collect(Collectors.toMap(
                    ComparisonField::getFieldName,
                    field -> products.stream()
                            .map(field.getExtractor())
                            .toList(),
                    (existing, replacement) -> existing,
                    LinkedHashMap::new
                ));
    }
}