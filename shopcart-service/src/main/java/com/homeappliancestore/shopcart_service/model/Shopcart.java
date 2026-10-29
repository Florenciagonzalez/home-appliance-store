package com.homeappliancestore.shopcart_service.model;

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
        if (items.contains(i)) return false;
        i.setShopcart(this);
        items.add(i);
        return true;
    }

}
