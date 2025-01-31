package com.alfarays.product.entity;

import com.alfarays.category.entity.Category;

import java.math.BigDecimal;

public class Product {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String sku;
    private Category category;

}
