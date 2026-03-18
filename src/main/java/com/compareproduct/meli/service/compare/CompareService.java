package com.compareproduct.meli.service.compare;

import com.compareproduct.meli.dto.compare.CompareResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CompareService {
    
    Mono<CompareResponse> compareProducts(List<Long> productIds) throws Exception;
}