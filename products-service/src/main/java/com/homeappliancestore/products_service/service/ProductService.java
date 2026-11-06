package com.homeappliancestore.products_service.service;

import com.homeappliancestore.products_service.dto.ProductDTO;
import com.homeappliancestore.products_service.exception.InvalidOperationException;
import com.homeappliancestore.products_service.exception.NotFoundException;
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
        Product p = productRepository.findById(id).orElseThrow(() -> new NotFoundException("El producto con la id " + id + " no fue encontrado"));
        return ProductMapper.toDTO(p);
    }

    @Override
    public ProductDTO save(ProductDTO p) {
        validateProduct(p);
       Product newProduct = Product.builder()
               .name(p.getName())
               .brand(p.getBrand())
               .unit_price(p.getUnit_price().setScale(2, java.math.RoundingMode.HALF_UP))
               .build();

        return ProductMapper.toDTO(productRepository.save(newProduct));
    }

    @Override
    public ProductDTO update(Long id, ProductDTO p) {
        if (!productRepository.existsById(id)) throw new NotFoundException("El producto con el id: " + id + " no fue encontrado");
        validateProduct(p);
        Product updated = Product.builder()
                .id_product(id)
                .name(p.getName())
                .brand(p.getBrand())
                .unit_price(p.getUnit_price().setScale(2, java.math.RoundingMode.HALF_UP))
                .build();

        return ProductMapper.toDTO(productRepository.save(updated));
    }

    @Override
    public void delete(Long id) {
        if (!productRepository.existsById(id)) throw new NotFoundException("El producto con el id: " + id + " no fue encontrado");
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDTO> findAllByIds(List<Long> ids) {
        return productRepository.findAllById(ids).stream().map(ProductMapper::toDTO).toList();
    }

    private void validateProduct(ProductDTO p){
        if (p.getName() == null || p.getName().isBlank()) throw new InvalidOperationException("El nombre del producto es obligatorio");
        if (p.getBrand() == null || p.getBrand().isBlank()) throw new InvalidOperationException("La marca del producto es obligatoria");
        if (p.getUnit_price() == null || p.getUnit_price().signum() <= 0) throw new InvalidOperationException("El precio debe ser mayor a 0");
    }
}
