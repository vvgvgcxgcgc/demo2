package com.example.demo.Service;

import com.example.demo.Domain.Order;
import com.example.demo.Domain.Product_Order;
import com.example.demo.dto.Orderdt;
import com.example.demo.dto.StatiticsbyDay;
import com.example.demo.dto.StatiticsbyMonth;
import com.example.demo.dto.StatiticsbyWeek;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface OrderService {
    public Order save(Orderdt orderdt);
    public Order save1(Orderdt orderdt, String username);


    // Product_Order save_productOrder(Long Orderid, List<String> Productid, List<Integer> quantity);
    // public Product_Order save_productOrder(Long Orderid, List<String> Productid, List<Integer> quantity);

    public Product_Order save_productOrder(Long Orderid, String Productid, Integer quantity);
    public List<Order> getAllOrders();
    public Order acceptOrder(Long id);
    public  Order cancelOrder(Long id);

    public Order getSuccessOrder(Long id);
    public  List<Order> getOrderofUser(String username);

    public StatiticsbyDay getDayRevenue();
    public StatiticsbyWeek getWeekRevenue();
    public StatiticsbyMonth getMonthRevenue();
    public Integer getPendingOrderamount();


}
