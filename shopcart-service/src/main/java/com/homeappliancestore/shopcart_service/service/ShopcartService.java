package com.homeappliancestore.shopcart_service.service;

import com.homeappliancestore.shopcart_service.dto.*;
import com.homeappliancestore.shopcart_service.exception.ExternalServiceUnavailableException;
import com.homeappliancestore.shopcart_service.exception.NotFoundException;
import com.homeappliancestore.shopcart_service.mapper.ShopcartMapper;
import com.homeappliancestore.shopcart_service.model.CartItem;
import com.homeappliancestore.shopcart_service.model.Shopcart;
import com.homeappliancestore.shopcart_service.repository.ICartItemRepository;
import com.homeappliancestore.shopcart_service.repository.IProductAPIClient;
import com.homeappliancestore.shopcart_service.repository.IShopcartRepository;
import feign.FeignException;
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
        Shopcart find = shopcartRepository.findById(id).orElseThrow(() -> new NotFoundException("El carrito con el id"+ id + "no fue encontrado"));

        return ShopcartMapper.toDTO(find);
    }

    @Override
    public ShopcartDTO create() {
        Shopcart s = Shopcart.builder().build();
        return ShopcartMapper.toDTO(shopcartRepository.save(s));
    }

    @Override
    public void addProduct(Long id, CartItemDTO item) {

        Shopcart shopcart = shopcartRepository.findById(id).orElseThrow(() -> new NotFoundException("El carrito con el id" + id + "no fue encontrado"));

        try {
            ProductDTO p = productAPIClient.getProduct(item.getId_product());
            shopcart.addItem(CartItem.builder()
                    .id_product(p.getId_product())
                    .quantity(item.getQuantity())
                    .build());
            shopcartRepository.save(shopcart);

        } catch (FeignException.NotFound exception){
            throw new NotFoundException("El product con el id " + item.getId_product() + "no fue encontrado");
        } catch (FeignException exception){
            throw new ExternalServiceUnavailableException("No se pudo consultar el servicio de productos");
        }
    }

    @Override
    public ShopcartWithProductsDTO getShopcartWithProducts(Long id) {
        Shopcart shopcart = shopcartRepository.findById(id).orElseThrow(() -> new NotFoundException("El carrito con el id"+ id + "no fue encontrado"));

        List<Long> id_products = shopcart.getItems().stream().map(CartItem::getId_product).toList();
        List<ProductDTO> products = new ArrayList<>();
        try {
            products = productAPIClient.findAllById(id_products);
        } catch (FeignException exception){
            throw new ExternalServiceUnavailableException("No se pudo consultar el servicio de productos");
        }

        Map<Long, ProductDTO> productsByIds = products.stream()
                    .collect(Collectors.toMap(ProductDTO::getId_product, product -> product));
            List<CartItemDetailDTO> details = new ArrayList<>();
            Double total_price = 0.0;

            for (CartItem item : shopcart.getItems()) {
                ProductDTO product = productsByIds.get(item.getId_product());

                CartItemDetailDTO detail = CartItemDetailDTO.builder()
                        .id_product(item.getId_product())
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
        if (!shopcartRepository.existsById(id)) throw new NotFoundException("El carrito con el id " + id + " no fue encontrado");

        shopcartRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteProduct(Long id, Long id_product) {
        if(!shopcartRepository.existsById(id)) throw new NotFoundException("El carrito con el id " + id + " no fue encontrado");
        if (!cartItemRepository.existsByShopcartIdAndIdProduct(id, id_product)) throw new NotFoundException("El producto con el id " + id_product + "no fue encontrado en el carrito con el id " + id);

        cartItemRepository.deleteByShopcartIdAndIdProduct(id, id_product);
    }
}
