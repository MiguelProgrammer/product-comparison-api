package com.compareproduct.meli.dto.compare;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CompareRequest(
    @NotEmpty(message = "{messages.compare.productIds.empty}")
    @Size(min = 2, max = 10, message = "{messages.compare.productIds.size}")
    List<Long> productIds
) {}