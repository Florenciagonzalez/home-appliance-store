package com.homeappliancestore.sales_service.service;
import com.homeappliancestore.sales_service.dto.*;
import com.homeappliancestore.sales_service.mapper.SaleMapper;
import com.homeappliancestore.sales_service.model.*;
import com.homeappliancestore.sales_service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class SaleService implements ISaleService {
    @Autowired
    private ISaleRepository saleRepository;
    @Autowired
    private IShopcartAPIClient shopcartAPIClient;


    @Override
    @Transactional
    public SaleDTO create(CreateSaleDTO request) {
        if (request.getShopcartId() == null || request.getShopcartId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del carrito debe ser positivo");
        }
        ShopcartWithProductsDTO cart = shopcartAPIClient.getShopcartWithProducts(request.getShopcartId());
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El carrito esta vacio");
        }
        Sale sale = Sale.builder().shopcartId(request.getShopcartId()).date(LocalDateTime.now()).build();
        sale.setItems(cart.getItems().stream().map(item -> {
            if (item.getId_product() == null || item.getQuantity() == null || item.getQuantity() <= 0
                    || item.getUnit_price() == null || item.getUnit_price().signum() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El carrito contiene un detalle invalido");
            }
            return SaleDetail.builder().id_product(item.getId_product()).name(item.getName())
                    .brand(item.getBrand()).quantity(item.getQuantity())
                    .unit_price(item.getUnit_price().setScale(2, RoundingMode.HALF_UP)).sale(sale).build();
        }).toList());
        sale.setTotal_price(sale.getItems().stream()
                .map(item -> item.getUnit_price().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return SaleMapper.toDTO(saleRepository.save(sale));
    }

    @Override
    public SaleDTO findById(Long id) {
        Sale sale = saleRepository.findById(id).orElseThrow();

        return SaleMapper.toDTO(sale);
    }
}
