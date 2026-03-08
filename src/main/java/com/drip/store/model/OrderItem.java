package com.drip.store.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;
    private String name;
    private String brand;
    private String emoji;
    private double price;
    private int qty;

    // Links this item back to its order
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore   // prevents infinite loop when converting to JSON
    private Order order;
}