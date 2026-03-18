package com.compareproduct.meli.controller.product;

import com.compareproduct.meli.dto.product.ProductRecord;
import com.compareproduct.meli.mapper.ProductMapper;
import com.compareproduct.meli.model.Product;
import com.compareproduct.meli.service.product.ProductService;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    
    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid ProductRecord productRecord) {
        Product product = productMapper.toProduct(productRecord);
        productService.create(product);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        productService.deleteAll();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        productService.deleteById(id);
    }

    @GetMapping
    public List<ProductRecord> getAll() {
        List<Product> products = productService.getAll();
        return products.stream()
                .map(productMapper::toProductRecord)
                .toList();
    }

    @GetMapping(value = "/{id}")
    public ProductRecord getById(@PathVariable Long id) throws Exception {
        Product product = productService.getById(id);
        return productMapper.toProductRecord(product);
    }
}