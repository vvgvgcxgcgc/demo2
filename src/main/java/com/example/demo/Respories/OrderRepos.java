package com.example.demo.Respories;

import com.example.demo.Domain.Order;
import com.example.demo.Domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface OrderRepos extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.usr.username = :username")
    List<Order> findAllByUserUsername(String username);

    @Query(value = "SELECT DISTINCT * FROM orders O WHERE O.time  BETWEEN  :startDate AND :endDate ",nativeQuery = true)
    public List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
