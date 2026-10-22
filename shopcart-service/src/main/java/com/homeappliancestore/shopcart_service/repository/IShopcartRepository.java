package com.homeappliancestore.shopcart_service.repository;

import com.homeappliancestore.shopcart_service.model.Shopcart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IShopcartRepository extends JpaRepository<Shopcart, Long> {
}
