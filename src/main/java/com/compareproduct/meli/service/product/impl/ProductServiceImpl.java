package com.compareproduct.meli.service.product.impl;

import com.compareproduct.meli.exception.BadResourceRequestException;
import com.compareproduct.meli.exception.NoSuchResourceFoundException;
import com.compareproduct.meli.mapper.ProductMapper;
import com.compareproduct.meli.model.Product;
import com.compareproduct.meli.entity.ProductEntity;
import com.compareproduct.meli.repository.ProductRepository;
import com.compareproduct.meli.service.product.ProductService;
import com.compareproduct.meli.telemetry.metrics.ProductMetrics;
import com.compareproduct.meli.util.MessageUtil;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductMetrics productMetrics;
    
    public ProductServiceImpl(ProductRepository productRepository, 
                            ProductMapper productMapper,
                            ProductMetrics productMetrics) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productMetrics = productMetrics;
    }

    @Override
    @CacheEvict(value = {"products", "product-comparisons"}, allEntries = true)
    public void deleteAll() {
        productRepository.deleteAllInBatch();
    }

    @Override
    @CacheEvict(value = {"products", "product-comparisons"}, allEntries = true)
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @CacheEvict(value = {"products", "product-comparisons"}, allEntries = true)
    @NewSpan("create-product")
    public void create(@SpanTag("productId") Product product) {
        log.debug("Creating product with ID: {}", product.getId());
        
        if (product.getId() != null) {
            Optional<ProductEntity> existingProduct = productRepository.findById(product.getId());
            if (existingProduct.isPresent()) {
                log.warn("Attempt to create duplicate product with ID: {}", product.getId());
                throw new BadResourceRequestException(MessageUtil.getMessage("messages.exception.product.duplicate"));
            }
        }
        
        ProductEntity entity = productMapper.toProductEntity(product);
        ProductEntity savedEntity = productRepository.save(entity);
        productMetrics.incrementProductCreated();
        log.info("Product created successfully with ID: {}", savedEntity.getId());
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    @NewSpan("get-product-by-id")
    public Product getById(@SpanTag("productId") Long id) throws Exception {
        log.debug("Fetching product with ID: {}", id);
        
        return productMetrics.recordProductFetchTime(() -> {
            Optional<ProductEntity> entity = productRepository.findById(id);
            return entity.map(productMapper::toProduct)
                    .orElseThrow(() -> {
                        productMetrics.incrementProductNotFound();
                        log.warn("Product not found with ID: {}", id);
                        return new NoSuchResourceFoundException(
                            MessageUtil.getMessage("messages.exception.product.notfound"));
                    });
        });
    }

    @Override
    public List<Product> getAll() {
        List<ProductEntity> entities = productRepository.findAll();
        return entities.stream()
                .map(productMapper::toProduct)
                .toList();
    }
}