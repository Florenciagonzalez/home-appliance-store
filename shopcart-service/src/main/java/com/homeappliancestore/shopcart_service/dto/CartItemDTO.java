package com.homeappliancestore.shopcart_service.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class CartItemDTO {
    private Long id;
    private Long id_product;
    private Integer quantity;
}
