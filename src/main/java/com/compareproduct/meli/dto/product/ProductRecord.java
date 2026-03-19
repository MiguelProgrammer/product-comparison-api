package com.compareproduct.meli.dto.product;

import com.compareproduct.meli.enums.Rate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRecord(

    Long id,

    @NotBlank(message = "{messages.product.name.notblank}")
    String name,
    
    @NotBlank(message = "{messages.product.description.notblank}")
    String description,
    
    @Positive(message = "{messages.product.price.positive}")
    Double price,
    
    @NotNull(message = "{messages.product.rating.notnull}")
    Rate rating,
    
    @NotBlank(message = "{messages.product.specification.notblank}")
    String specification,
    
    @NotBlank(message = "{messages.product.url.notblank}")
    String url
) {}
