package com.homeappliancestore.products_service.model;

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
    private Double unit_price;
}
