package com.homeappliancestore.shopcart_service.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class CartItemDetailDTO {
    private String name;
    private String brand;
    private Double unit_price;
    private Integer quantity;
}
