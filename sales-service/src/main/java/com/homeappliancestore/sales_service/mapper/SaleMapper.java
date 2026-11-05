package com.homeappliancestore.sales_service.mapper;
import com.homeappliancestore.sales_service.dto.*;
import com.homeappliancestore.sales_service.model.Sale;
public class SaleMapper {
    public static SaleDTO toDTO(Sale sale) {
        return SaleDTO.builder()
                .id(sale.getId())
                .shopcartId(sale.getShopcartId())
                .date(sale.getDate())
                .total_price(sale.getTotal_price())
                .items(sale.getItems().stream().map(item -> CartItemDetailDTO.builder()
                        .id_product(item.getId_product()).name(item.getName()).brand(item.getBrand())
                        .quantity(item.getQuantity()).unit_price(item.getUnit_price()).build()).toList())
                .build();
    }
}
