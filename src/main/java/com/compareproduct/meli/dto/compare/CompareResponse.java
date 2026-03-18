package com.compareproduct.meli.dto.compare;

import com.compareproduct.meli.enums.Rate;

import java.util.List;
import java.util.Map;

public record CompareResponse(
    List<ProductSummary> products,
    Map<String, List<Object>> comparison
) {
    
    public record ProductSummary(
        Long id,
        String name,
        String description,
        Double price,
        Rate rating,
        String specification,
        String url
    ) {}
}