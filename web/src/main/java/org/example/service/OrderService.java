package org.example.service;

import org.example.entity.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {
    public Order createMemberOrder() {
        LocalDateTime now = LocalDateTime.now();

        Order newOrder = new Order();

        return newOrder;
    }
}
