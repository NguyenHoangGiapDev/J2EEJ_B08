package com.example.productmanager.controller;

import com.example.productmanager.entity.Product;
import com.example.productmanager.repository.CategoryRepository;
import com.example.productmanager.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductService productService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
public String listProducts(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "sort", required = false) String sort,
        @RequestParam(value = "categoryId", required = false) Long categoryId,
        @RequestParam(value = "page", defaultValue = "1") int page,
        Model model) {

    int pageSize = 5;
    var productPage = productService.getProductsByPage(page, pageSize, keyword, sort, categoryId);

    model.addAttribute("products", productPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", productPage.getTotalPages());

    model.addAttribute("keyword", keyword);
    model.addAttribute("sort", sort);
    model.addAttribute("selectedCategoryId", categoryId);
    model.addAttribute("categories", categoryRepository.findAll());

    return "products/list";
}

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "products/form";
    }

    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult result,
                              @RequestParam(value = "categoryId", required = false) Long categoryId,
                              Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "products/form";
        }

        if (categoryId != null) {
            categoryRepository.findById(categoryId).ifPresent(product::setCategory);
        } else {
            product.setCategory(null);
        }

        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "products/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}