package com.example.productmanager;

import com.example.productmanager.entity.Category;
import com.example.productmanager.entity.Product;
import com.example.productmanager.repository.CategoryRepository;
import com.example.productmanager.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataLoader(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void loadData() {
        if (categoryRepository.count() == 0) {
            Category laptop = categoryRepository.save(new Category("Laptop"));
            Category phone = categoryRepository.save(new Category("Điện thoại"));
            Category accessory = categoryRepository.save(new Category("Phụ kiện"));

            if (productRepository.count() == 0) {
                productRepository.save(new Product(
                        "Lenovo ThinkPad T15 15.6\" Laptop Intel Core i7-10610U 512GB SSD 16GB RAM FHD",
                        "https://images.unsplash.com/photo-1517336714739-489689fd1ca8?w=600",
                        27000,
                        laptop
                ));

                productRepository.save(new Product(
                        "iPhone 16 Pro Max 1TB",
                        "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600",
                        41990,
                        phone
                ));

                productRepository.save(new Product(
                        "Chuột không dây Logitech",
                        "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=600",
                        850,
                        accessory
                ));
            }
        }
    }
}