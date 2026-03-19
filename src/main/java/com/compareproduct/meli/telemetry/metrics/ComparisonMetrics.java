package com.compareproduct.meli.telemetry.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
public class ComparisonMetrics {

    private final Counter comparisonRequestCounter;
    private final Timer comparisonTimer;

    public ComparisonMetrics(MeterRegistry meterRegistry) {
        this.comparisonRequestCounter = Counter.builder("comparisons.requests.total")
                .description("Total number of comparison requests")
                .register(meterRegistry);
                
        this.comparisonTimer = Timer.builder("comparisons.duration")
                .description("Time taken to complete product comparisons")
                .register(meterRegistry);
    }

    public void incrementComparisonRequest() {
        comparisonRequestCounter.increment();
    }

    public void recordComparisonRequest() {
        comparisonRequestCounter.increment();
    }

    public void recordComparisonDuration(long durationMs) {
        comparisonTimer.record(durationMs, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    public <T> T recordComparisonTime(Callable<T> operation) throws Exception {
        return comparisonTimer.recordCallable(operation);
    }
}