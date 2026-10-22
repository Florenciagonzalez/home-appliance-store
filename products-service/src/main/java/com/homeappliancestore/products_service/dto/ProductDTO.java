package com.homeappliancestore.products_service.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class ProductDTO {
    private Long id_product;
    private String name;
    private String brand;
    private Double unit_price;
}
