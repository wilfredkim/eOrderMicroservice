package com.wilfred.order.orderservice.service;

import com.wilfred.order.orderservice.events.OrderKafkaEvent;
import com.wilfred.order.orderservice.model.Order;
import com.wilfred.order.orderservice.model.OrderItem;
import com.wilfred.order.orderservice.payload.*;
import com.wilfred.order.orderservice.repository.OrderRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final ObservationRegistry observationRegistry;

    private final KafkaTemplate<String, OrderKafkaEvent> kafkaTemplate;

    @Override
    public Order placeAnOrder(OrderRequest orderRequest) {
        log.info("Placing an order >>>>>>>>>>>>>>.");
        log.info("orderRequest>>>>>>>>>>>>>>>>> "+orderRequest.toString());
        Order order = new Order();
        List<OrderItem> orderItemList = orderRequest.getOrderItemsRequests().
                stream().map(this::mapToOrderItemsDto).toList();
        order.setOrderItems(orderItemList);
        order.setOrderNumber(UUID.randomUUID().toString());
        List<String> orderLineItemsSkucode = order.getOrderItems().stream().map(OrderItem::getSkuCode).toList();
        Observation inventoryServiceObservation = Observation.createNotStarted("inventory-service-lookup",
                this.observationRegistry);
        inventoryServiceObservation.lowCardinalityKeyValue("call", "inventory-service");
        return inventoryServiceObservation.observe(() -> {
            InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", orderLineItemsSkucode).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            assert inventoryResponses != null;
            boolean aBoolean = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
            if (Boolean.TRUE.equals(aBoolean)) {
                Order save = orderRepository.save(order);
                kafkaTemplate.send("notificationTopic", new OrderKafkaEvent(save.getOrderNumber()));
                return save;
            } else throw new IllegalArgumentException("Product Not in stock, please try again!");
        });


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
