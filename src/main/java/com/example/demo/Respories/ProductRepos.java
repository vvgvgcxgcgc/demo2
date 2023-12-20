package com.example.demo.Respories;

import com.example.demo.Domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepos extends JpaRepository<Product,String> {
    @Query("SELECT p.Image FROM Product p WHERE p.id = :id")
    String findProductImageById(String id);
//    @Query("SELECT * FROM Product p WHERE p.id = :id")
//    String
}