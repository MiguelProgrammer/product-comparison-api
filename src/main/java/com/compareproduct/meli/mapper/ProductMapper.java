package com.compareproduct.meli.mapper;

import com.compareproduct.meli.dto.compare.CompareResponse;
import com.compareproduct.meli.dto.product.ProductRecord;
import com.compareproduct.meli.entity.ProductEntity;
import com.compareproduct.meli.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toProduct(ProductRecord productRecord);

    ProductRecord toProductRecord(Product product);

    ProductEntity toProductEntity(Product product);

    Product toProduct(ProductEntity productEntity);

    CompareResponse.ProductSummary toProductSummary(Product product);
}