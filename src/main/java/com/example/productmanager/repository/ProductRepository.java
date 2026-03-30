package com.example.productmanager.repository;

import com.example.productmanager.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByNameContainingIgnoreCase(String keyword, Sort sort);
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByCategoryId(Long categoryId, Sort sort);
}