package com.compareproduct.meli.telemetry.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
public class ProductMetrics {

    private final Counter productCreatedCounter;
    private final Counter productNotFoundCounter;
    private final Timer productFetchTimer;

    public ProductMetrics(MeterRegistry meterRegistry) {
        this.productCreatedCounter = Counter.builder("products.created.total")
                .description("Total number of products created")
                .register(meterRegistry);
                
        this.productNotFoundCounter = Counter.builder("products.notfound.total")
                .description("Total number of product not found errors")
                .register(meterRegistry);
                
        this.productFetchTimer = Timer.builder("products.fetch.duration")
                .description("Time taken to fetch individual products")
                .register(meterRegistry);
    }

    public void incrementProductCreated() {
        productCreatedCounter.increment();
    }

    public void incrementProductNotFound() {
        productNotFoundCounter.increment();
    }

    public <T> T recordProductFetchTime(Callable<T> operation) throws Exception {
        return productFetchTimer.recordCallable(operation);
    }
}