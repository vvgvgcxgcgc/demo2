package com.example.demo.Respositories;

import com.example.demo.Domain.Order;
import com.example.demo.dto.BlackListPhoneNumber;
import com.example.demo.dto.Top3Productdt;
import com.example.demo.dto.Top3Userdt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface OrderRepos extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.usr.username = :username")
    List<Order> findAllByUserUsername(String username);

    @Query(value = "SELECT DISTINCT * FROM orders O WHERE O.time  BETWEEN  :startDate AND :endDate ",nativeQuery = true)
    public List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value ="SELECT new com.example.demo.dto.Top3Productdt( P, SUM(PO.amount) ) FROM Product_Order PO, Order O, Product P WHERE P.id = PO.pro.id AND O.id =PO.od.id AND( O.time BETWEEN  :startDate AND :endDate) AND O.orderstatus = 3 GROUP BY PO.pro.id ORDER BY SUM(PO.amount) DESC LIMIT 3")
    public List<Top3Productdt> findTop3Product(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT new com.example.demo.dto.Top3Userdt(O.usr,SUM(O.total)) FROM Order O WHERE O.orderstatus = 3  AND ( O.time BETWEEN  :startDate AND :endDate) GROUP BY O.usr.id ORDER BY SUM(O.total) DESC LIMIT 3")
    public List<Top3Userdt> findTop3User(LocalDateTime startDate, LocalDateTime endDate);
    @Query(value = "SELECT O FROM Order O WHERE O.orderstatus = 1 ")
    public List<Order> findPendingOrders();

    @Query(value = "SELECT  new com.example.demo.dto.BlackListPhoneNumber(O.unregister_phonenumber,count(O.id)) FROM Order O WHERE O.orderstatus = 4 GROUP BY O.unregister_phonenumber HAVING count(O.id) >= 3 ORDER BY count(O.id) DESC ")
    public List<BlackListPhoneNumber> findBlackList();


}
