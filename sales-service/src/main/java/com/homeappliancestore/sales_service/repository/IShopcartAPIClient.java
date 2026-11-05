package com.homeappliancestore.sales_service.repository;
import com.homeappliancestore.sales_service.dto.ShopcartWithProductsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
@FeignClient(name = "shopcart-service")
public interface IShopcartAPIClient {
    @GetMapping("/api/shopcarts/{id}/products")
    ShopcartWithProductsDTO getShopcartWithProducts(@PathVariable("id") Long id);
}
