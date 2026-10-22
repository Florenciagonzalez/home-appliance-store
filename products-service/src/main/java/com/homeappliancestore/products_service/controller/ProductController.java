package com.homeappliancestore.products_service.controller;

import com.homeappliancestore.products_service.dto.ProductDTO;
import com.homeappliancestore.products_service.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping("")
    public ResponseEntity<List<ProductDTO>> getAll(){
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProductDTO> getDetail(@PathVariable Long id){
        return ResponseEntity.ok(productService.getDetail(id));
    }

    @PostMapping("")
    public ResponseEntity<ProductDTO> save(@RequestBody ProductDTO p){
        ProductDTO newProduct = productService.save(p);
        return ResponseEntity.created(URI.create("/api/products/" + newProduct.getId_product())).body(newProduct);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO p){
        return ResponseEntity.ok(productService.update(id, p));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/listOfProducts")
    public ResponseEntity<List<ProductDTO>> findAllByIds(@RequestBody List<Long> ids){
        return ResponseEntity.ok(productService.findAllByIds(ids));
    }
}
