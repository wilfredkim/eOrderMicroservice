package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.payload.InventoryResponse;
import com.example.inventorymanagement.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam("skuCode") List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }
}
