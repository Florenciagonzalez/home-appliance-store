package com.homeappliancestore.sales_service.controller;
import com.homeappliancestore.sales_service.dto.*;
import com.homeappliancestore.sales_service.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    @Autowired
    private ISaleService saleService;

    @PostMapping("")
    public ResponseEntity<SaleDTO> create(@RequestBody CreateSaleDTO request) {
        SaleDTO created = saleService.create(request);
        return ResponseEntity.created(URI.create("/api/sales/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(saleService.findById(id));
    }
}
