package com.compareproduct.meli.telemetry.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelemetryConfig {
    
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config()
                .commonTags("application", "product-comparison-api")
                .commonTags("version", "1.0.0")
                .commonTags("environment", "local")
                .meterFilter(MeterFilter.deny(id -> {
                    String name = id.getName();
                    // Filtra métricas desnecessárias para reduzir ruído
                    return name.startsWith("jvm.threads.daemon") ||
                           name.startsWith("jvm.classes.loaded") ||
                           name.startsWith("process.files") ||
                           name.startsWith("system.load.average");
                }));
    }
    
    @Bean
    public HealthIndicator customHealthIndicator() {
        return () -> Health.up()
                .withDetail("app", "product-comparison-api")
                .withDetail("version", "1.0.0")
                .withDetail("description", "API para comparação de produtos")
                .build();
    }
    
}