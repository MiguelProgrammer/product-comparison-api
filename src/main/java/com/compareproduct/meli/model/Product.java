package com.compareproduct.meli.model;

import com.compareproduct.meli.enums.Rate;

public class Product {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Rate raiting;
    private String specification;
    private String url;

    public Product() {
    }

    public Product(Long id, String name, String description, Double price, Rate raiting, String specification, String url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.raiting = raiting;
        this.specification = specification;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Rate getRaiting() {
        return raiting;
    }

    public void setRaiting(Rate raiting) {
        this.raiting = raiting;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
