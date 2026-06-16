package com.orderService.mapper;

import com.orderService.dto.CreateOrderRequest;
import com.orderService.entity.Order;
import com.orderService.enm.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderMapper {

    public static Order toEntity(CreateOrderRequest request,
                                 BigDecimal amount){

        Order order = new Order();

        order.setUserId(request.getUserId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());

        order.setAmount(amount);
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        return order;
    }

}