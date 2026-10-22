package com.homeappliancestore.products_service.service;

import com.homeappliancestore.products_service.dto.ProductDTO;

import java.util.List;

public interface IProductService {
    List<ProductDTO> getAll();
    ProductDTO getDetail(Long id);
    ProductDTO save(ProductDTO p);
    ProductDTO update(Long id, ProductDTO p);
    void delete(Long id);
    List<ProductDTO> findAllByIds(List<Long> ids);
}
