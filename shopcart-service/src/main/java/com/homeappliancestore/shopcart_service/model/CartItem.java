package com.homeappliancestore.shopcart_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long id_product;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "shopcart_id")
    private Shopcart shopcart;



}
