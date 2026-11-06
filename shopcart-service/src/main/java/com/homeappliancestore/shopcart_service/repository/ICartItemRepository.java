package com.homeappliancestore.shopcart_service.repository;

import com.homeappliancestore.shopcart_service.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("select (count(c) > 0) from CartItem c where c.shopcart.id = :id and c.id_product = :productId")
    boolean existsByShopcartIdAndIdProduct(@Param("id") Long id, @Param("productId") Long id_product);

    @Modifying
    @Query("delete from CartItem c where c.shopcart.id = :id and c.id_product = :productId")
    void deleteByShopcartIdAndIdProduct(@Param("id") Long id, @Param("productId") Long id_product);
}
