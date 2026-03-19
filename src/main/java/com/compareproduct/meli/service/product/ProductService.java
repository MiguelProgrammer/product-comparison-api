package com.compareproduct.meli.service.product;

import com.compareproduct.meli.model.Product;
import reactor.core.publisher.Flux;
import java.util.List;

public interface ProductService {

    void deleteAll();
    void deleteById(Long id);
    void create(Product product);

    Product getById(Long id) throws Exception;
    List<Product> getAll();
    Flux<Product> getByIds(List<Long> ids);
}
