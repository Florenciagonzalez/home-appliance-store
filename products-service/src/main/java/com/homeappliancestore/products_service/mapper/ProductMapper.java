package com.homeappliancestore.products_service.mapper;

import com.homeappliancestore.products_service.dto.ProductDTO;
import com.homeappliancestore.products_service.model.Product;

public class ProductMapper {

    public static ProductDTO toDTO(Product p){
        return ProductDTO.builder()
                .id_product(p.getId_product())
                .name(p.getName())
                .brand(p.getBrand())
                .unit_price(p.getUnit_price())
                .build();
    }
}
