package com.homeappliancestore.shopcart_service.mapper;

import com.homeappliancestore.shopcart_service.dto.CartItemDetailDTO;
import com.homeappliancestore.shopcart_service.dto.ShopcartDTO;
import com.homeappliancestore.shopcart_service.dto.ShopcartWithProductsDTO;
import com.homeappliancestore.shopcart_service.model.Shopcart;

import java.util.List;

public class ShopcartMapper {

    public static ShopcartDTO toDTO(Shopcart s){
        return ShopcartDTO.builder()
                .id(s.getId())
                .build();
    }

    public static ShopcartWithProductsDTO toDTO(Long id, List<CartItemDetailDTO> items, Double total_price){
        return ShopcartWithProductsDTO.builder()
                .id(id)
                .items(items)
                .total_price(total_price)
                .build();
    }
}
