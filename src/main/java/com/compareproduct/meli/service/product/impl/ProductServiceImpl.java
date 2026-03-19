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
import reactor.core.publisher.Flux;

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
        log.info("PRODUCT_DELETE_ALL_STARTED");
        try {
            long count = productRepository.count();
            productRepository.deleteAllInBatch();
            log.info("PRODUCT_DELETE_ALL_SUCCESS deletedCount={}", count);
        } catch (Exception e) {
            log.error("PRODUCT_DELETE_ALL_ERROR error={}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @CacheEvict(value = {"products", "product-comparisons"}, allEntries = true)
    public void deleteById(Long id) {
        log.info("PRODUCT_DELETE_STARTED productId={}", id);
        try {
            productRepository.deleteById(id);
            log.info("PRODUCT_DELETE_SUCCESS productId={}", id);
        } catch (Exception e) {
            log.error("PRODUCT_DELETE_ERROR productId={} error={}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @CacheEvict(value = {"products", "product-comparisons"}, allEntries = true)
    @NewSpan("create-product")
    public void create(@SpanTag("productId") Product product) {
        log.info("PRODUCT_CREATE_STARTED productId={} productName={}", product.getId(), product.getName());
        
        if (product.getId() != null) {
            Optional<ProductEntity> existingProduct = productRepository.findById(product.getId());
            if (existingProduct.isPresent()) {
                log.error("PRODUCT_CREATE_ERROR reason=duplicate_id productId={}", product.getId());
                throw new BadResourceRequestException(MessageUtil.getMessage("messages.product.duplicate"));
            }
        }
        
        try {
            ProductEntity entity = productMapper.toProductEntity(product);
            ProductEntity savedEntity = productRepository.save(entity);
            productMetrics.incrementProductCreated();
            log.info("PRODUCT_CREATE_SUCCESS productId={} productName={}", savedEntity.getId(), savedEntity.getName());
        } catch (Exception e) {
            log.error("PRODUCT_CREATE_ERROR productId={} error={}", product.getId(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    @NewSpan("get-product-by-id")
    public Product getById(@SpanTag("productId") Long id) throws Exception {
        log.debug("PRODUCT_GET_STARTED productId={}", id);
        
        return productMetrics.recordProductFetchTime(() -> {
            Optional<ProductEntity> entity = productRepository.findById(id);
            return entity.map(productEntity -> {
                Product product = productMapper.toProduct(productEntity);
                log.debug("PRODUCT_GET_SUCCESS productId={} productName={}", id, product.getName());
                return product;
            })
            .orElseThrow(() -> {
                productMetrics.incrementProductNotFound();
                log.warn("PRODUCT_GET_ERROR reason=not_found productId={}", id);
                return new NoSuchResourceFoundException(
                    MessageUtil.getMessage("messages.product.notfound"));
            });
        });
    }

    @Override
    public List<Product> getAll() {
        log.debug("PRODUCT_GET_ALL_STARTED");
        try {
            List<ProductEntity> entities = productRepository.findAll();
            List<Product> products = entities.stream()
                    .map(productMapper::toProduct)
                    .toList();
            log.info("PRODUCT_GET_ALL_SUCCESS count={}", products.size());
            return products;
        } catch (Exception e) {
            log.error("PRODUCT_GET_ALL_ERROR error={}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Flux<Product> getByIds(List<Long> ids) {

        log.debug("PRODUCT_GET_BY_IDS_STARTED ids={}", ids);

        try {
            List<ProductEntity> entities = productRepository.findAllById(ids);
            List<Product> products = entities.stream()
                    .map(productMapper::toProduct)
                    .toList();

            log.info("PRODUCT_GET_BY_IDS_SUCCESS requestedCount={} foundCount={}", ids.size(), products.size());
            return Flux.fromIterable(products);

        } catch (Exception e) {
            log.error("PRODUCT_GET_BY_IDS_ERROR ids={} error={}", ids, e.getMessage(), e);
            return Flux.error(e);
        }
    }
}