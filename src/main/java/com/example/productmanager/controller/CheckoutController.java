package com.example.productmanager.controller;

import com.example.productmanager.entity.CartItem;
import com.example.productmanager.entity.Order;
import com.example.productmanager.entity.OrderDetail;
import com.example.productmanager.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final OrderRepository orderRepository;

    public CheckoutController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @SuppressWarnings("unchecked")
    @GetMapping
    public String checkoutPage(HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cartItems", cart);
        model.addAttribute("total", calculateTotal(cart));

        Order orderForm = new Order();
        orderForm.setUsername("user");
        model.addAttribute("orderForm", orderForm);

        return "checkout/index";
    }

    @SuppressWarnings("unchecked")
    @PostMapping
    public String placeOrder(@ModelAttribute("orderForm") Order orderForm,
                             HttpSession session,
                             Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setUsername(orderForm.getUsername());
        order.setFullName(orderForm.getFullName());
        order.setPhone(orderForm.getPhone());
        order.setAddress(orderForm.getAddress());

        double total = 0;
        List<OrderDetail> details = new ArrayList<>();

        for (CartItem item : cart) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getProduct().getPrice());
            detail.setSubtotal(item.getSubtotal());

            total += item.getSubtotal();
            details.add(detail);
        }

        order.setTotalAmount(total);
        order.setOrderDetails(details);

        orderRepository.save(order);

        session.removeAttribute("cart");

        return "redirect:/cart?success";
    }

    private double calculateTotal(List<CartItem> cart) {
        return cart.stream().mapToDouble(CartItem::getSubtotal).sum();
    }
}