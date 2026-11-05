package com.homeappliancestore.sales_service.dto;
import lombok.*;
import java.math.BigDecimal;
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class CartItemDetailDTO {
    private Long id_product;
    private String name;
    private String brand;
    private BigDecimal unit_price;
    private Integer quantity;
}
