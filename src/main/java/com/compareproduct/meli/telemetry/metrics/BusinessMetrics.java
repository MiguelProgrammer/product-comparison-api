package com.compareproduct.meli.telemetry.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BusinessMetrics {

    private final Counter successfulComparisonsCounter;
    private final Counter failedComparisonsCounter;
    private final Counter productsComparedCounter;
    private final AtomicInteger activeComparisons;

    public BusinessMetrics(MeterRegistry meterRegistry) {
        this.successfulComparisonsCounter = Counter.builder("business.comparisons.successful.total")
                .description("Total number of successful product comparisons")
                .register(meterRegistry);
                
        this.failedComparisonsCounter = Counter.builder("business.comparisons.failed.total")
                .description("Total number of failed product comparisons")
                .register(meterRegistry);
        
        this.productsComparedCounter = Counter.builder("business.products.compared.total")
                .description("Total number of products that have been compared")
                .register(meterRegistry);
        
        this.activeComparisons = new AtomicInteger(0);
        
        // Gauge registration - forma mais simples que funciona
        meterRegistry.gauge("business.comparisons.active.current", this.activeComparisons);
    }

    public void recordSuccessfulComparison(int productCount) {
        successfulComparisonsCounter.increment();
        productsComparedCounter.increment(productCount);
    }

    public void recordFailedComparison() {
        failedComparisonsCounter.increment();
    }

    public void incrementActiveComparisons() {
        activeComparisons.incrementAndGet();
    }

    public void decrementActiveComparisons() {
        activeComparisons.decrementAndGet();
    }

    public void recordActiveComparisons(int delta) {
        activeComparisons.addAndGet(delta);
    }

    public double getSuccessfulComparisons() {
        return successfulComparisonsCounter.count();
    }

    public double getFailedComparisons() {
        return failedComparisonsCounter.count();
    }

    public double getSuccessRate() {
        double total = getSuccessfulComparisons() + getFailedComparisons();
        return total > 0 ? (getSuccessfulComparisons() / total) * 100 : 0;
    }

    public int getActiveComparisons() {
        return activeComparisons.get();
    }
}