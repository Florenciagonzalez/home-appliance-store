package com.homeappliancestore.sales_service.dto;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class SaleDTO {
    private Long id;
    private Long shopcartId;
    private LocalDateTime date;
    private BigDecimal total_price;
    private List<CartItemDetailDTO> items;
}
