package com.homeappliancestore.products_service.repository;

import com.homeappliancestore.products_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    @Override
    List<Product> findAllById(Iterable<Long> ids);
}
