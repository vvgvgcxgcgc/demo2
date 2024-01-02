package com.example.demo.Respories;

import com.example.demo.Domain.Order;
import com.example.demo.Domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepos extends JpaRepository<Order, Long> {
}
