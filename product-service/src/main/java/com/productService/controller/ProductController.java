package com.productService.controller;

import com.common.dto.ResponseDto;
import com.productService.entity.Product;
import com.productService.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<ResponseDto> get() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(products));
    }

//    @PostMapping("/getAllTask")
//    public ResponseEntity<ResponseDto> getAllTask() {
//        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        List<Product> list  = productService.getAllTask(userPrincipal.getUsername());
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(list));
//    }
//
//    @PostMapping("/update")
//    public ResponseEntity<ResponseDto> update(@RequestBody ProductDto productDto) {
//        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        productService.updateTask(productDto,userPrincipal.getUsername());
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Task updated successfully."));
//    }

}
