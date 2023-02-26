package com.wilfred.order.orderservice.controller;

import com.wilfred.order.orderservice.model.Order;
import com.wilfred.order.orderservice.payload.OrderRequest;
import com.wilfred.order.orderservice.payload.OrderResponse;
import com.wilfred.order.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.placeAnOrder(orderRequest);
        return order != null ? "Order Placed Successfully" : "Could not place an order please try again!";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getList() {
        return orderService.getList();
    }
}
