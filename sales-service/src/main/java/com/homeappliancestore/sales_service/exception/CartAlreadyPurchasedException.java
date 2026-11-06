package com.homeappliancestore.sales_service.exception;

public class CartAlreadyPurchasedException extends RuntimeException {
    public CartAlreadyPurchasedException(Long shopcartId) {
        super("El carrito con ID " + shopcartId + " ya tiene una venta registrada");
    }
}
