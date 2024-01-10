package com.example.demo.Respories;

import com.example.demo.Domain.Product;
import com.example.demo.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.Domain.User;

import java.time.LocalDateTime;
import java.util.List;


@Repository

public interface UserRepo extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User findByUsername(String username);

    @Query(value = "SELECT COUNT(O.id) FROM Order O, Product_Order PO WHERE O.id= PO.od.id AND O.usr.id =:userid AND PO.pro.id =:productid")
    public int productOrderTimes(long userid, String productid);

    @Query(value = "SELECT U FROM User U WHERE U.Checkuser = 1 AND  U.time BETWEEN  :startDate AND :endDate")
    public List<User> findNewUsers(LocalDateTime startDate, LocalDateTime endDate);
}
