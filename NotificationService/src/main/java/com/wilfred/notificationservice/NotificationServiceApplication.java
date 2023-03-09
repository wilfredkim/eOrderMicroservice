package com.wilfred.notificationservice;

import com.wilfred.notificationservice.event.OrderKafkaEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotifications(OrderKafkaEvent orderKafkaEvent) {
        log.info("Notification for Oder , {}", orderKafkaEvent.getOrderNumber());
    }

}
