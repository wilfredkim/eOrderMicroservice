package com.wilfred.order.orderservice.repository;

import com.wilfred.order.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
