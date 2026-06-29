package com.productService.service;


import com.productService.entity.Product;
import com.productService.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


//    public long createTask(ProductDto productDto, String user) {
//        Product product = ProductMapper.createTask(productDto,user);
//        product = productRepository.save(product);
//        return product.getId();
//    }
//
//    public List<Product> getAllTask(String username) {
//        return productRepository.findAllByAssignedTo(username);
//    }
//
//    public void updateTask(ProductDto productDto, String user) {
//        if(productDto.getStatus().equals(EnumTaskStatus.OPEN)){
//            throw new BusinessException("Status cannot be open while updating the task.");
//        }
//        Product product = productRepository.findById(productDto.getId()).orElseThrow(() -> new NotFoundException("Task not found with the given id : "+ productDto.getId()));
//        product.setStatus(productDto.getStatus());
//        product.setRemarks(productDto.getRemarks());
//        product.setCompletedTime(LocalDateTime.now());
//        productRepository.save(product);
//        return;
//    }
}
