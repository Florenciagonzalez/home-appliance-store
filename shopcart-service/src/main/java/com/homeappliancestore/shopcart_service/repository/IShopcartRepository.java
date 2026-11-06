package com.homeappliancestore.shopcart_service.repository;

import com.homeappliancestore.shopcart_service.model.Shopcart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface IShopcartRepository extends JpaRepository<Shopcart, Long> {
    @Query(value = "select * from shopcart where id = :id for update", nativeQuery = true)
    Optional<Shopcart> findByIdForUpdate(@Param("id") Long id);
}
