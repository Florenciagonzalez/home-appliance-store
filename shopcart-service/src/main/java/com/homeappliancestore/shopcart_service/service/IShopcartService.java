package com.homeappliancestore.shopcart_service.service;

import com.homeappliancestore.shopcart_service.dto.CartItemDTO;
import com.homeappliancestore.shopcart_service.dto.ShopcartDTO;
import com.homeappliancestore.shopcart_service.dto.ShopcartWithProductsDTO;

import java.util.List;

public interface IShopcartService {
    List<ShopcartDTO> getAll();
    ShopcartDTO findById(Long id);
    ShopcartWithProductsDTO getShopcartWithProducts(Long id);
    ShopcartDTO create();
    void addProduct(Long id, CartItemDTO item);
    void delete(Long id);
    void deleteProduct(Long id, Long id_product);
}
