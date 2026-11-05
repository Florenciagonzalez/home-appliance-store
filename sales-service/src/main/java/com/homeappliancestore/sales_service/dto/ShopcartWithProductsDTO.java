package com.homeappliancestore.sales_service.dto;
import lombok.*;
import java.util.List;
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class ShopcartWithProductsDTO {
    private Long id;
    private List<CartItemDetailDTO> items;
}
