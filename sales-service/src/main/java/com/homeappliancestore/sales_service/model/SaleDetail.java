package com.homeappliancestore.sales_service.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@Entity
public class SaleDetail {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long id_product;
    private String name;
    private String brand;
    @Column(precision = 19, scale = 2)
    private BigDecimal unit_price;
    private Integer quantity;
    @ManyToOne @JoinColumn(name = "sale_id")
    private Sale sale;
}
