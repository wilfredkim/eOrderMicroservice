package com.wilfred.product.productmanagement.repository;

import com.wilfred.product.productmanagement.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
