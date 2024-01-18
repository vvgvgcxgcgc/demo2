package com.example.demo.Respositories;

import com.example.demo.Domain.Product;
import com.example.demo.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository

public interface UserRepo extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User findByUsername(String username);

    @Query(value = "SELECT COUNT(O.id) FROM Order O, Product_Order PO WHERE O.id= PO.od.id AND O.usr.id =:userid AND PO.pro.id =:productid")
    public int productOrderTimes(long userid, String productid);

    @Query(value = "SELECT U FROM User U WHERE U.Checkuser = 1 AND  U.time BETWEEN  :startDate AND :endDate")
    public List<User> findNewUsers(LocalDateTime startDate, LocalDateTime endDate);
    @Query(value = " SELECT U FROM User U WHERE U.Checkuser = 1 AND U.Userpoint <= :top AND U.Userpoint>=:min_point AND U.Userpoint>= :down")
    List<User> findUserInRange(int down, int top, int min_point);
}
