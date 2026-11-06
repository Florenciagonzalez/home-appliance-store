package com.homeappliancestore.shopcart_service.model;

import com.homeappliancestore.shopcart_service.exception.InvalidOperationException;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Builder
public class Shopcart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "shopcart", cascade = CascadeType.ALL)
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();


    public boolean addItem(CartItem i){
        for (CartItem existing : items) {
            if (existing.getId_product().equals(i.getId_product())) {
                try {
                    existing.setQuantity(Math.addExact(existing.getQuantity(), i.getQuantity()));
                } catch (ArithmeticException exception) {
                    throw new InvalidOperationException("La cantidad excede el limite permitido");
                }
                return true;
            }
        }
        i.setShopcart(this);
        items.add(i);
        return true;
    }

}
