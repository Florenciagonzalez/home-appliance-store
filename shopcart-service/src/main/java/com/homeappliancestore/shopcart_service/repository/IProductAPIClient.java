package com.homeappliancestore.shopcart_service.repository;

import com.homeappliancestore.shopcart_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "products-service")
public interface IProductAPIClient {
    @GetMapping("/api/products/id/{id}")
    ProductDTO getProduct(@PathVariable Long id);

    @PostMapping("/api/products/listOfProducts")
    List<ProductDTO> findAllById(@RequestBody List<Long> ids);

}
