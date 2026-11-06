package com.homeappliancestore.sales_service.repository;
import com.homeappliancestore.sales_service.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ISaleRepository extends JpaRepository<Sale, Long> {
    boolean existsByShopcartId(Long shopcartId);
}
