package com.wilfred.product.productmanagement.model;

import lombok.*;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "product")
@Builder
public class Product {
    @Id
    @NonNull
    private String id;

    private String name;

    private String description;

    private BigDecimal price;
}
