package com.homeappliancestore.sales_service.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_sale_shopcart", columnNames = "shopcart_id"))
public class Sale {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "shopcart_id", nullable = false)
    private Long shopcartId;
    private LocalDateTime date;
    @Column(precision = 19, scale = 2)
    private BigDecimal total_price;
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleDetail> items;
}
