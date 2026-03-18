package com.compareproduct.meli.enums;

import com.compareproduct.meli.model.Product;
import java.util.function.Function;

public enum ComparisonField {
    
    PRICE("price", Product::getPrice),
    RATING("rating", Product::getRaiting),
    SPECIFICATION("specification", Product::getSpecification);
    
    private final String fieldName;
    private final Function<Product, Object> extractor;
    
    ComparisonField(String fieldName, Function<Product, Object> extractor) {
        this.fieldName = fieldName;
        this.extractor = extractor;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public Function<Product, Object> getExtractor() {
        return extractor;
    }
}