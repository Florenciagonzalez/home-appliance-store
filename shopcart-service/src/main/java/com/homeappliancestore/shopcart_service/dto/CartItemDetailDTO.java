package com.homeappliancestore.shopcart_service.dto;

import java.math.BigDecimal;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class CartItemDetailDTO {
    private Long id_product;
    private String name;
    private String brand;
    private BigDecimal unit_price;
    private Integer quantity;
}
