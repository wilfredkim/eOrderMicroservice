package com.wilfred.order.orderservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsRequest {
    private String skuCode;
    private BigDecimal price;
    private int quantity;
}
