package com.example.inventorymanagement;

import com.example.inventorymanagement.model.Inventory;
import com.example.inventorymanagement.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryManagementApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
        return args -> {
            Inventory inventory = new Inventory();
            inventory.setSkuCode("Fridge");
            inventory.setQuantity(100);
            Inventory inventory1 = new Inventory();
            inventory1.setSkuCode("Fridge2");
            inventory1.setQuantity(0);
            inventoryRepository.save(inventory);
            inventoryRepository.save(inventory1);
        };
    }
}
