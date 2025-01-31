package com.alfarays.inventory.entity;

import com.alfarays.product.entity.Product;

public class Inventory {

    private Long id;
    private Product product;
    private Integer quantityInStock;
    private Integer reorderLevel;
    private Integer safetyStock;

}
