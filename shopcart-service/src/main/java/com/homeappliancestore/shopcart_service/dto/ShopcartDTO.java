package com.homeappliancestore.shopcart_service.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class ShopcartDTO {
    private Long id;
    private Double total_price;
    private List<Long> products;
}
