package com.wilfred.product.productmanagement.service;

import com.wilfred.product.productmanagement.model.Product;
import com.wilfred.product.productmanagement.payload.ProductRequest;
import com.wilfred.product.productmanagement.payload.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductService {
    Product createProduct(ProductRequest productRequest);

    List<ProductResponse> getAllProducts();
}
