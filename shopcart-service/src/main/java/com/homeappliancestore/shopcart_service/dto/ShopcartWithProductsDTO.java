package com.homeappliancestore.shopcart_service.dto;

import java.math.BigDecimal;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class ShopcartWithProductsDTO {
    private Long id;
    private BigDecimal total_price;
    private List<CartItemDetailDTO> items;
}
