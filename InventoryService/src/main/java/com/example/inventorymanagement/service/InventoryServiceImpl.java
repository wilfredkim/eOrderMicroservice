package com.example.inventorymanagement.service;

import com.example.inventorymanagement.payload.InventoryResponse;
import com.example.inventorymanagement.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional()
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode).stream().
                map(inventory -> InventoryResponse.builder()
                        .isInStock(inventory.getQuantity() > 0)
                        .skuCode(inventory.getSkuCode()).build()
                ).toList();

    }
}
