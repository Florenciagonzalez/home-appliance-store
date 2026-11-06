package com.homeappliancestore.sales_service.exception;

import com.homeappliancestore.sales_service.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(CartAlreadyPurchasedException.class)
    public ResponseEntity<ErrorResponse> handleCartAlreadyPurchased(
            CartAlreadyPurchasedException exception, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .code("CART_ALREADY_PURCHASED")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException exception, HttpServletRequest request){
        ErrorResponse error = ErrorResponse.builder()
                .code("RESOURCE_NOT_FOUND")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOperation(InvalidOperationException exception, HttpServletRequest request){
        ErrorResponse error = ErrorResponse.builder()
                .code("BAD_REQUEST")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(ExternalServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceUnavailable(ExternalServiceUnavailableException exception, HttpServletRequest request){
        ErrorResponse error = ErrorResponse.builder()
                .code("SERVICE_UNAVAILABLE")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(error);
    }
}
