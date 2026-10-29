package com.homeappliancestore.shopcart_service.controller;

import com.homeappliancestore.shopcart_service.dto.CartItemDTO;
import com.homeappliancestore.shopcart_service.dto.ShopcartDTO;
import com.homeappliancestore.shopcart_service.dto.ShopcartWithProductsDTO;
import com.homeappliancestore.shopcart_service.service.IShopcartService;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/shopcarts")
public class ShopcartController {
    @Autowired
    private IShopcartService shopcartService;

    @GetMapping("")
    public ResponseEntity<List<ShopcartDTO>> getAll(){
        return ResponseEntity.ok(shopcartService.getAll());
    }

    @PostMapping("")
    public ResponseEntity<ShopcartDTO> create(){
        ShopcartDTO created = shopcartService.create();
        return ResponseEntity.created(URI.create("/api/shopcarts/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopcartDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(shopcartService.findById(id));
    }

    @PostMapping("/{id}/addProduct")
    public ResponseEntity<Void> addItem(@PathVariable Long id, @RequestBody CartItemDTO item){
        shopcartService.addProduct(id, item);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<ShopcartWithProductsDTO> getShopcartWithProducts(@PathVariable Long id){
        ShopcartWithProductsDTO shopcart = shopcartService.getShopcartWithProducts(id);
        return ResponseEntity.ok(shopcart);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        shopcartService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/products/{id_product}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, @PathVariable Long id_product){
        shopcartService.deleteProduct(id, id_product);
        return ResponseEntity.noContent().build();
    }
}
