package com.productService.mapper;

import com.productService.dto.ProductDto;
import com.productService.entity.Product;

public class ProductMapper {

    public static Product createTask(ProductDto productDto, String user) {

        Product product = new Product();
//        product.setName(productDto.getTaskName());
//        product.setDescription(productDto.getTaskDescription());
//        product.setCreationTime(LocalDateTime.now());
//        product.setAssignedTo(user);
//        product.setStatus(EnumTaskStatus.OPEN);
        return product;
    }
}
