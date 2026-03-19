package com.compareproduct.meli.model;

import com.compareproduct.meli.enums.Rate;

public class Product {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Rate rating;
    private String specification;
    private String url;

    public Product() {
    }

    public Product(Long id, String name, String description, Double price, Rate rating, String specification, String url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.specification = specification;
        this.url = url;
    }

    public static Builder builder() {
        return new Builder();
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

    public Rate getRating() {
        return rating;
    }

    public void setRating(Rate rating) {
        this.rating = rating;
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

    public static class Builder {
        private Long id;
        private String name;
        private String description;
        private Double price;
        private Rate rating;
        private String specification;
        private String url;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder rating(Rate rating) {
            this.rating = rating;
            return this;
        }

        public Builder specification(String specification) {
            this.specification = specification;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Product build() {
            return new Product(id, name, description, price, rating, specification, url);
        }
    }
}