package com.homeappliancestore.shopcart_service.dto;

import java.math.BigDecimal;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class ProductDTO {
    private Long id_product;
    private String name;
    private String brand;
    private BigDecimal unit_price;

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id_product +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", unit_price=" + unit_price +
                '}';
    }
}
