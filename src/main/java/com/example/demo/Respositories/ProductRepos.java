package com.example.demo.Respositories;

import com.example.demo.Domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepos extends JpaRepository<Product,String> {
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findById(String id);
    @Query("SELECT p.Image FROM Product p WHERE p.id = :id")
    String findProductImageById(String id);

    @Query(value = "SELECT P FROM Product P, Feedback F WHERE P.id = F.product.id AND F.status = 1 GROUP BY P.id ORDER BY COUNT(F.id) DESC LIMIT 6")
    List<Product> findTop6ProductFeedback();

    @Query( "SELECT P FROM Product P WHERE (P.Name LIKE %?1 OR P.Info LIKE %?1%) AND P.deleted = false")
    List<Product> findProductByName(String name);

    @Query(value = "SELECT P.Name FROM Product P WHERE  P.deleted = false ")
    List<String> findAllProductName();


}
