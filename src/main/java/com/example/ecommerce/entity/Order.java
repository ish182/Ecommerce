package com.example.ecommerce.entity;


import jakarta.persistence.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity

@Table(name
        = "orders")

public class Order {


    @Id
    @GeneratedValue

    private Long
            id;

    private String
            userEmail;

    private LocalDateTime orderDate;

    private double totalAmount;

    @OneToMany(mappedBy
            = "order", cascade = CascadeType.ALL)

    private List<OrderItem> items = new ArrayList<>();

    public void setUserEmail(String name) {
        this.userEmail = name;
    }
    public String getUserEmail() {
        return userEmail;
    }

    public void setOrderDate(LocalDateTime now) {
        this.orderDate = now;
    }
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getId() {
        return"";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @SpringBootApplication
    public static class EcommerceApplication {

        public static void main(String[] args) {
            SpringApplication.run(EcommerceApplication.class, args);
  }

}
}
