package com.example.demo.Respories;

import com.example.demo.Domain.Product_Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Product_OrderRepos extends JpaRepository<Product_Order, Long> {
}
