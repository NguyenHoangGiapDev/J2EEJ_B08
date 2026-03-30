package com.example.productmanager.service;

import com.example.productmanager.entity.Product;
import com.example.productmanager.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> searchAndSortAndFilter(String keyword, String sort, Long categoryId) {
        Sort sortOption = buildSort(sort);

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasCategory = categoryId != null;

        if (hasKeyword && hasCategory) {
            return productRepository.findAll(sortOption).stream()
                    .filter(p -> p.getName() != null &&
                                 p.getName().toLowerCase().contains(keyword.trim().toLowerCase()))
                    .filter(p -> p.getCategory() != null &&
                                 p.getCategory().getId().equals(categoryId))
                    .toList();
        }

        if (hasKeyword) {
            return productRepository.findByNameContainingIgnoreCase(keyword.trim(), sortOption);
        }

        if (hasCategory) {
            return productRepository.findByCategoryId(categoryId, sortOption);
        }

        return productRepository.findAll(sortOption);
    }

    @Override
    public Page<Product> getProductsByPage(int pageNo, int pageSize, String keyword, String sort, Long categoryId) {
        List<Product> filteredList = searchAndSortAndFilter(keyword, sort, categoryId);

        int start = (pageNo - 1) * pageSize;
        int end = Math.min(start + pageSize, filteredList.size());

        List<Product> pageContent;
        if (start >= filteredList.size()) {
            pageContent = List.of();
        } else {
            pageContent = filteredList.subList(start, end);
        }

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return new PageImpl<>(pageContent, pageable, filteredList.size());
    }

    private Sort buildSort(String sort) {
        if ("asc".equalsIgnoreCase(sort)) {
            return Sort.by("price").ascending();
        }
        if ("desc".equalsIgnoreCase(sort)) {
            return Sort.by("price").descending();
        }
        return Sort.unsorted();
    }
}