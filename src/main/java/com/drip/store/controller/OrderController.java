package com.drip.store.controller;

import com.drip.store.model.Order;
import com.drip.store.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // ── POST /api/orders ───────────────────────────────────────────────
    // Called by checkout.js when user places an order
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("pending");
        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    // ── GET /api/orders ────────────────────────────────────────────────
    // Admin: returns all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // ── GET /api/orders/{id} ───────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── PUT /api/orders/{id}/status ────────────────────────────────────
    // Admin: update order status (pending → processing → shipped → delivered)
    // Body: { "status": "shipped" }
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        return orderRepository.findById(id).map(order -> {
            String newStatus = body.get("status");
            if (newStatus != null && !newStatus.isBlank()) {
                order.setStatus(newStatus);
                orderRepository.save(order);
            }
            return ResponseEntity.ok(order);
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── DELETE /api/orders/{id} ────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}