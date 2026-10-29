package com.homeappliancestore.shopcart_service.service;

import com.homeappliancestore.shopcart_service.dto.*;
import com.homeappliancestore.shopcart_service.mapper.ShopcartMapper;
import com.homeappliancestore.shopcart_service.model.CartItem;
import com.homeappliancestore.shopcart_service.model.Shopcart;
import com.homeappliancestore.shopcart_service.repository.ICartItemRepository;
import com.homeappliancestore.shopcart_service.repository.IProductAPIClient;
import com.homeappliancestore.shopcart_service.repository.IShopcartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShopcartService implements IShopcartService{
    @Autowired
    private IShopcartRepository shopcartRepository;
    @Autowired
    private IProductAPIClient productAPIClient;
    @Autowired
    private ICartItemRepository cartItemRepository;


    @Override
    public List<ShopcartDTO> getAll() {
        return shopcartRepository.findAll().stream().map(ShopcartMapper::toDTO).toList();
    }

    @Override
    public ShopcartDTO findById(Long id) {
        Shopcart find = shopcartRepository.findById(id).orElseThrow(() -> new RuntimeException("El carrito no fue encontrado"));

        return ShopcartMapper.toDTO(find);
    }

    @Override
    public ShopcartDTO create() {
        Shopcart s = Shopcart.builder().build();
        return ShopcartMapper.toDTO(shopcartRepository.save(s));
    }

    @Override
    public void addProduct(Long id, CartItemDTO item) {

        Shopcart shopcart = shopcartRepository.findById(id).orElseThrow(() -> new RuntimeException("El carrito no fue encontrado"));

        ProductDTO p = productAPIClient.getProduct(item.getId_product());

        shopcart.addItem(CartItem.builder()
                .id_product(p.getId_product())
                .quantity(item.getQuantity())
                .build());

        shopcartRepository.save(shopcart);
    }

    @Override
    public ShopcartWithProductsDTO getShopcartWithProducts(Long id) {
        Shopcart shopcart = shopcartRepository.findById(id).orElseThrow();

        List<Long> id_products = shopcart.getItems().stream().map(CartItem::getId_product).toList();
        List<ProductDTO> products = productAPIClient.findAllById(id_products);

        Map<Long, ProductDTO> productsByIds = products.stream()
                .collect(Collectors.toMap(ProductDTO::getId_product, product -> product));
        List<CartItemDetailDTO> details = new ArrayList<>();
        Double total_price = 0.0;

        for (CartItem item : shopcart.getItems()){
            ProductDTO product = productsByIds.get(item.getId_product());

            CartItemDetailDTO detail = CartItemDetailDTO.builder()
                    .name(product.getName())
                    .brand(product.getBrand())
                    .unit_price(product.getUnit_price())
                    .quantity(item.getQuantity())
                    .build();

            details.add(detail);

            total_price += product.getUnit_price() * item.getQuantity();
        }

        return ShopcartMapper.toDTO(shopcart.getId(), details, total_price);
    }

    @Override
    public void delete(Long id) {
        if (!shopcartRepository.existsById(id)) throw new RuntimeException("El carrito con la id " + id + " no fue encontrado");

        shopcartRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteProduct(Long id, Long id_product) {
        if(!shopcartRepository.existsById(id)) throw new RuntimeException("El carrito con la id " + id + " no fue encontrado");
        if (!cartItemRepository.existsByShopcartIdAndIdProduct(id, id_product)) throw new RuntimeException("El producto no fue encontrado en el carrito");

        cartItemRepository.deleteByShopcartIdAndIdProduct(id, id_product);
    }
}
