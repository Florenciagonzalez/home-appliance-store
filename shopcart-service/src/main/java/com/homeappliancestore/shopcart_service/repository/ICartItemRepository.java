package com.homeappliancestore.shopcart_service.repository;

import com.homeappliancestore.shopcart_service.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem, Long> {
    boolean existsByShopcartIdAndIdProduct(Long id, Long id_product);
    void deleteByShopcartIdAndIdProduct(Long id, Long id_product);
}
