package com.orderService.controller;

import com.common.dto.ResponseDto;
import com.common.security.UserPrincipal;
import com.orderService.dto.CreateOrderRequest;
import com.orderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createOrder(
            @RequestBody CreateOrderRequest request) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        request.setUserId(Long.valueOf(principal.getUsername()));
        long id = service.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(id));

    }

}