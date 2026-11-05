package com.homeappliancestore.sales_service.service;
import com.homeappliancestore.sales_service.dto.*;
public interface ISaleService {
    SaleDTO create(CreateSaleDTO request);
    SaleDTO findById(Long id);
}
