package com.wilfred.order.orderservice.service;

import com.wilfred.order.orderservice.model.Order;
import com.wilfred.order.orderservice.model.OrderItem;
import com.wilfred.order.orderservice.payload.OrderItemsRequest;
import com.wilfred.order.orderservice.payload.OrderItemsResponse;
import com.wilfred.order.orderservice.payload.OrderRequest;
import com.wilfred.order.orderservice.payload.OrderResponse;
import com.wilfred.order.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final WebClient webClient;

    private final Tracer tracer;

    @Override
    public Order placeAnOrder(OrderRequest orderRequest) {
        Order order = new Order();
        List<OrderItem> orderItemList = orderRequest.getOrderItemsRequests().
                stream().map(this::mapToOrderItemsDto).toList();
        order.setOrderItems(orderItemList);
        order.setOrderNumber(UUID.randomUUID().toString());
        List<String> orderLineItemsSkucode = order.getOrderItems().stream().map(OrderItem::getSkuCode).toList();

        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");
        try (Tracer.SpanInScope ignored = tracer.withSpan(inventoryServiceLookup.start())) {
            //call inventory service
            Boolean aBoolean = webClient.get().uri("http://INVENTORY-SERVICE/api/v1/inventories",
                    uriBuilder -> uriBuilder.queryParam("skuCode", orderLineItemsSkucode).
                            build()).retrieve().bodyToMono(boolean.class).block();
            if (Boolean.TRUE.equals(aBoolean)) return orderRepository.save(order);
            else throw new IllegalArgumentException("Product Not in stock, please try again!");
        } finally {
            inventoryServiceLookup.end();
        }


    }

    private OrderItem mapToOrderItemsDto(OrderItemsRequest orderItemsRequest) {
        return OrderItem.builder()
                .skuCode(orderItemsRequest.getSkuCode())
                .price(orderItemsRequest.getPrice()).quantity(orderItemsRequest.getQuantity()).
                build();
    }

    @Override
    public List<OrderResponse> getList() {
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream().map(this::mapToDTO).toList();
    }

    private OrderResponse mapToDTO(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderItemsResponses(order.getOrderItems().stream().map(this::mapToOrderItemsDto).toList())
                .build();
    }

    private OrderItemsResponse mapToOrderItemsDto(OrderItem orderItem) {
        return OrderItemsResponse.builder()
                .skuCode(orderItem.getSkuCode())
                .price(orderItem.getPrice()).quantity(orderItem.getQuantity()).
                build();
    }
}
