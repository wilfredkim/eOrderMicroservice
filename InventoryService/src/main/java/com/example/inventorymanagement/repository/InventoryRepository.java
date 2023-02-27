package com.example.inventorymanagement.repository;

import com.example.inventorymanagement.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {


    List<Inventory> findBySkuCodeIn(List<String> skuCodes);
}
