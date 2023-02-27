package com.example.inventorymanagement.service;

import com.example.inventorymanagement.payload.InventoryResponse;

import java.util.List;

public interface InventoryService {
    List<InventoryResponse> isInStock(List<String> skuCode);
}
