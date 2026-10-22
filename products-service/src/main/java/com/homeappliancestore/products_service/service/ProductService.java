package com.homeappliancestore.products_service.service;

import com.homeappliancestore.products_service.dto.ProductDTO;
import com.homeappliancestore.products_service.mapper.ProductMapper;
import com.homeappliancestore.products_service.model.Product;
import com.homeappliancestore.products_service.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService{
    @Autowired
    private IProductRepository productRepository;

    @Override
    public List<ProductDTO> getAll() {
        return productRepository.findAll().stream().map(ProductMapper::toDTO).toList();
    }

    @Override
    public ProductDTO getDetail(Long id) {
        Product p = productRepository.findById(id).orElseThrow(() -> new RuntimeException("El producto con la id " + id + " no fue encontrado"));
        return ProductMapper.toDTO(p);
    }

    @Override
    public ProductDTO save(ProductDTO p) {
       Product newProduct = Product.builder()
               .name(p.getName())
               .brand(p.getBrand())
               .unit_price(p.getUnit_price())
               .build();

        return ProductMapper.toDTO(productRepository.save(newProduct));
    }

    @Override
    public ProductDTO update(Long id, ProductDTO p) {
        if (!productRepository.existsById(id)) throw new RuntimeException("El producto con el id: " + id + " no fue encontrado");

        Product updated = Product.builder()
                .id_product(p.getId_product())
                .name(p.getName())
                .brand(p.getBrand())
                .unit_price(p.getUnit_price())
                .build();

        return ProductMapper.toDTO(productRepository.save(updated));
    }

    @Override
    public void delete(Long id) {
        if (!productRepository.existsById(id)) throw new RuntimeException("El producto con el id: " + id + " no fue encontrado");

        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDTO> findAllByIds(List<Long> ids) {
        return productRepository.findAllById(ids).stream().map(ProductMapper::toDTO).toList();
    }
}
