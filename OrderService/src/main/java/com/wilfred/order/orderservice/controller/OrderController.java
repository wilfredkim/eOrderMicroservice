package com.wilfred.order.orderservice.controller;

import com.wilfred.order.orderservice.model.Order;
import com.wilfred.order.orderservice.payload.OrderRequest;
import com.wilfred.order.orderservice.payload.OrderResponse;
import com.wilfred.order.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackPlaceOrder")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.placeAnOrder(orderRequest);
        return order != null ? CompletableFuture.supplyAsync(()->"Order Placed Successfully") : CompletableFuture.supplyAsync(()->"Could not place an order please try again!");
    }

    public CompletableFuture<String> fallbackPlaceOrder(OrderRequest orderRequest, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(()->"Oops! Something went wrong please retry after a while") ;

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getList() {
        return orderService.getList();
    }
}
