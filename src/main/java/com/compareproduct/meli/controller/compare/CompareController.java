package com.compareproduct.meli.controller.compare;

import com.compareproduct.meli.dto.compare.CompareRequest;
import com.compareproduct.meli.dto.compare.CompareResponse;
import com.compareproduct.meli.service.compare.CompareService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/compare")
public class CompareController {

    private final CompareService compareService;
    
    public CompareController(CompareService compareService) {
        this.compareService = compareService;
    }
    
    @PostMapping(value = "/products", consumes = "application/json")
    @NewSpan("compare-products-endpoint")
    @RateLimiter(name = "comparison-api")
    public Mono<CompareResponse> compareProducts(@RequestBody @Valid @SpanTag("request") CompareRequest request) {
        return compareService.compareProducts(request.productIds());
    }
}