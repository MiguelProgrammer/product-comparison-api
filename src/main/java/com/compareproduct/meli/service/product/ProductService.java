package com.compareproduct.meli.service.product;

import com.compareproduct.meli.model.Product;
import java.util.List;

public interface ProductService {

    void deleteAll();
    void deleteById(Long id);
    void create(Product product);

    Product getById(Long id) throws Exception;
    List<Product> getAll();
}
