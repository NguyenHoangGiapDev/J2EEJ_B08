package com.example.productmanager.service;

import com.example.productmanager.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product saveProduct(Product product);
    void deleteProduct(Long id);

    List<Product> searchAndSortAndFilter(String keyword, String sort, Long categoryId);

    Page<Product> getProductsByPage(int pageNo, int pageSize, String keyword, String sort, Long categoryId);
}