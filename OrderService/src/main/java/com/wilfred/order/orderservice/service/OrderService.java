package com.wilfred.order.orderservice.service;

import com.wilfred.order.orderservice.model.Order;
import com.wilfred.order.orderservice.payload.OrderRequest;
import com.wilfred.order.orderservice.payload.OrderResponse;

import java.util.List;

public interface OrderService {

    Order placeAnOrder(OrderRequest orderRequest);

    List<OrderResponse> getList();
}
