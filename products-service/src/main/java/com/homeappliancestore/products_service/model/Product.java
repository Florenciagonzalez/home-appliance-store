package com.homeappliancestore.products_service.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

// código, nombre, marca y precio individual

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_product;
    private String name;
    private String brand;
    @jakarta.persistence.Column(precision = 19, scale = 2)
    private BigDecimal unit_price;
}
