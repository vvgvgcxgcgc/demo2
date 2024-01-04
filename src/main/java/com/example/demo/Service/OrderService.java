package com.example.demo.Service;

import com.example.demo.Domain.Order;
import com.example.demo.Domain.Product_Order;
import com.example.demo.dto.Orderdt;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface OrderService {
    public Order save(Orderdt orderdt);


    // Product_Order save_productOrder(Long Orderid, List<String> Productid, List<Integer> quantity);
    // public Product_Order save_productOrder(Long Orderid, List<String> Productid, List<Integer> quantity);

    public Product_Order save_productOrder(Long Orderid, String Productid, Integer quantity);
}
