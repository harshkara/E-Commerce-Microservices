package com.orderService.service;

import com.orderService.dto.CreateOrderRequest;
import com.orderService.entity.Order;
import com.orderService.mapper.OrderMapper;
import com.orderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    public Long createOrder(CreateOrderRequest request){

        // Temporary until Product Service integration
        BigDecimal productPrice = BigDecimal.valueOf(90000);

        BigDecimal amount =
                productPrice.multiply(BigDecimal.valueOf(request.getQuantity()));

        Order order = OrderMapper.toEntity(request, amount);

        repository.save(order);

        return order.getId();

    }

}