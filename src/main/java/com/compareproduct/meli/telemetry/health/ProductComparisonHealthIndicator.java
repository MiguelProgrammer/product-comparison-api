package com.compareproduct.meli.telemetry.health;

import com.compareproduct.meli.telemetry.metrics.BusinessMetrics;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("productComparisonHealth")
public class ProductComparisonHealthIndicator implements HealthIndicator {

    private final BusinessMetrics businessMetrics;

    public ProductComparisonHealthIndicator(BusinessMetrics businessMetrics) {
        this.businessMetrics = businessMetrics;
    }

    @Override
    public Health health() {
        Health.Builder builder = new Health.Builder();
        
        // Verifica taxa de sucesso
        double successRate = businessMetrics.getSuccessRate();
        boolean isHealthy = successRate >= 95.0 || businessMetrics.getSuccessfulComparisons() < 10;
        
        if (isHealthy) {
            builder.up();
        } else {
            builder.down();
        }
        
        return builder
                .withDetail("successRate", String.format("%.2f%%", successRate))
                .withDetail("totalComparisons", businessMetrics.getSuccessfulComparisons() + businessMetrics.getFailedComparisons())
                .withDetail("activeComparisons", businessMetrics.getActiveComparisons())
                .build();
    }
}