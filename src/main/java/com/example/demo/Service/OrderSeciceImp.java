package com.example.demo.Service;

import com.example.demo.Domain.Order;
import com.example.demo.Domain.Product;
import com.example.demo.Domain.Product_Order;
import com.example.demo.Respories.OrderRepos;
import com.example.demo.Respories.Product_OrderRepos;
import com.example.demo.dto.Orderdt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service

public class OrderSeciceImp implements OrderService {
    private final ProductService productService;
    private final OrderRepos orderRepos;
    private final Product_OrderRepos productOrderRepos;



    @Override
    public Order save(Orderdt orderdt) {
      Order order = Order.builder()
              .time(LocalDateTime.now())
              .orderstatus(1)
              .unregister_name(orderdt.getName())
              .unregister_address(orderdt.getAddress())
              .unregister_phonenumber(orderdt.getPhonenumber())
              .order_productList(new ArrayList<>())
              .total(orderdt.getTotalPrice())
              .build();

//      for(int i=0;i<Productid.size()-1;i++){
//          Product p = productService.getProductById(Productid.get(i));
//          Product_Order po = Product_Order.builder()
//                  .od(order)
//                  .pro(p)
//                  .amount(quantity.get(i))
//                  .build();
//
//          order.getOrder_productList().add(po);
//         // productOrderRepos.save(po);
//
//      }

      return orderRepos.save(order);
    }

    @Override
    public Product_Order save_productOrder(Long Orderid, String Productid, Integer quantity) {
        Order order = orderRepos.getReferenceById(Orderid);
        Product product = productService.getProductById(Productid);
        Product_Order productOrder = Product_Order.builder()
                .pro(product)
                .od(order)
                .amount(quantity)
                .build();
        return productOrderRepos.save(productOrder);
    }
}
