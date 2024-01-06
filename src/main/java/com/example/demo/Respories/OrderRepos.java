package com.example.demo.Respories;

import com.example.demo.Domain.Order;
import com.example.demo.Domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepos extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.usr.username = :username")
    List<Order> findAllByUserUsername(String username);
}
