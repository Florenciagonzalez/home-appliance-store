package com.homeappliancestore.sales_service.service;
import com.homeappliancestore.sales_service.dto.*;
import com.homeappliancestore.sales_service.exception.ExternalServiceUnavailableException;
import com.homeappliancestore.sales_service.exception.InvalidOperationException;
import com.homeappliancestore.sales_service.exception.NotFoundException;
import com.homeappliancestore.sales_service.mapper.SaleMapper;
import com.homeappliancestore.sales_service.model.*;
import com.homeappliancestore.sales_service.repository.*;
import feign.FeignException;
import com.homeappliancestore.sales_service.exception.CartAlreadyPurchasedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        validateRequest(request);
        if (saleRepository.existsByShopcartId(request.getShopcartId())) {
            throw new CartAlreadyPurchasedException(request.getShopcartId());
        }
        ShopcartWithProductsDTO cart = getShopcart(request.getShopcartId());
        validateShopcart(cart);
        Sale sale = buildSale(request.getShopcartId(), cart);
        return SaleMapper.toDTO(saveSale(sale));
    }

    private Sale saveSale(Sale sale) {
        try {
            return saleRepository.saveAndFlush(sale);
        } catch (DataIntegrityViolationException exception) {
            for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
                if (cause instanceof ConstraintViolationException violation
                        && violation.getConstraintName() != null
                        && violation.getConstraintName().toLowerCase(java.util.Locale.ROOT)
                                .contains("uk_sale_shopcart")) {
                    throw new CartAlreadyPurchasedException(sale.getShopcartId());
                }
            }
            throw exception;
        }
    }

    private void validateRequest(CreateSaleDTO request) {
        if (request.getShopcartId() == null || request.getShopcartId() <= 0) {
            throw new InvalidOperationException("El id del carrito no puede estar vacío o ser negativo.");
        }
    }

    private ShopcartWithProductsDTO getShopcart(Long shopcartId) {
        try {
            return shopcartAPIClient.getShopcartWithProducts(shopcartId);
        } catch (FeignException.NotFound exception) {
            throw new NotFoundException("El carrito o uno de sus productos no existe");
        } catch (FeignException.BadRequest exception) {
            throw new InvalidOperationException("El carrito no es valido para registrar una venta");
        } catch (FeignException exception){
            throw new ExternalServiceUnavailableException("No se pudo establecer la conexión con el servicio shopcart");
        }
    }

    private void validateShopcart(ShopcartWithProductsDTO cart) {
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new InvalidOperationException("El carrito esta vacio");
        }
        for (CartItemDetailDTO item : cart.getItems()) {
            if (item.getId_product() == null || item.getQuantity() == null || item.getQuantity() <= 0
                    || item.getUnit_price() == null || item.getUnit_price().signum() < 0) {
                throw new InvalidOperationException("El carrito contiene un detalle invalido");
            }
        }
    }

    private Sale buildSale(Long shopcartId, ShopcartWithProductsDTO cart) {
        Sale sale = Sale.builder().shopcartId(shopcartId).date(LocalDateTime.now()).build();
        sale.setItems(cart.getItems().stream().map(item -> {
            return SaleDetail.builder().id_product(item.getId_product()).name(item.getName())
                    .brand(item.getBrand()).quantity(item.getQuantity())
                    .unit_price(item.getUnit_price().setScale(2, RoundingMode.HALF_UP)).sale(sale).build();
        }).toList());
        sale.setTotal_price(sale.getItems().stream()
                .map(item -> item.getUnit_price().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return sale;
    }

    @Override
    public SaleDTO findById(Long id) {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new NotFoundException("La compra con el id " + id + " no fue encontrada."));
        return SaleMapper.toDTO(sale);
    }
}
