package com.example.demo.Service;

import com.example.demo.Domain.Order;
import com.example.demo.Domain.Product;
import com.example.demo.Domain.Product_Order;
import com.example.demo.Domain.User;
import com.example.demo.Respories.OrderRepos;
import com.example.demo.Respories.Product_OrderRepos;
import com.example.demo.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service

public class OrderSeciceImp implements OrderService {
    private final ProductService productService;
    private final OrderRepos orderRepos;
    private final Product_OrderRepos productOrderRepos;
    private final Userser userser;



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
    public Order save1(Orderdt orderdt, String username) {
        User user = userser.findByUsername(username);
        Order order = Order.builder()
                .usr(user)
                .orderstatus(1)
                .unregister_name(user.getFullname())
                .unregister_phonenumber(user.getPhonenumber())
                .order_productList(new ArrayList<>())
                .time(LocalDateTime.now())
                .total(orderdt.getTotalPrice())
                .unregister_address(orderdt.getAddress())
                .build();
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

    @Override
    public List<Order> getAllOrders() {
        return orderRepos.findAll();
    }

    @Override
    public Order acceptOrder(Long id) {
        Order order = orderRepos.getReferenceById(id);
        order.setOrderstatus(2);
        return orderRepos.save(order);
    }

    @Override
    public Order cancelOrder(Long id) {
        Order order = orderRepos.getReferenceById(id);
        order.setOrderstatus(4);
        return orderRepos.save(order);
    }

    @Override
    public Order getSuccessOrder(Long id) {
        Order order = orderRepos.getReferenceById(id);
        order.setOrderstatus(3);
        return orderRepos.save(order);
    }

    @Override
    public List<Order> getOrderofUser(String username) {
        return orderRepos.findAllByUserUsername(username);
    }

    @Override
    public StatiticsbyDay getDayRevenue() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime timenow = LocalDateTime.now();
        long orderSuccess =0;

        List<Order> orders= orderRepos.findByOrderDateBetween(startOfToday,timenow);
        long sum=0;
        for(Order order: orders){
            if(order.getOrderstatus()==3) {
                sum += order.getTotal();
                orderSuccess++;
            }
        }
        StatiticsbyDay statiticsbyDay = StatiticsbyDay.builder()
                .orderSuccessAmount(orderSuccess)
                .revenuebyDay(sum)
                .build();
        return statiticsbyDay;

    }

    @Override
    public StatiticsbyWeek getWeekRevenue() {
        LocalDateTime startOfWeek = LocalDateTime.now().with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
        LocalDateTime timenow = LocalDateTime.now();
        List<Integer> orderthroughhour = new ArrayList<>(Collections.nCopies(25, 0));

        List<Order> orders= orderRepos.findByOrderDateBetween(startOfWeek,timenow);
        long sum=0;
        long orderTotal =0;
        long orderSuccess =0;
        long orderCancel =0;
        double orderrate ;

        List<Top3Productdt> top3product = orderRepos.findTop3Product(startOfWeek, timenow);
        for(Order order: orders){
            if(order.getOrderstatus()==3) {
                sum += order.getTotal();
                orderSuccess ++;
                int index = (int)order.getTime().getHour();
                int value = orderthroughhour.get(index) +1;
                orderthroughhour.set(0,value);

            }
            else if(order.getOrderstatus()==4)  orderCancel ++;
        }
        orderrate = (double)orderCancel/(orderCancel+orderSuccess) * 100;
        StatiticsbyWeek statiticsbyWeek = StatiticsbyWeek.builder()
                .revenuebyWeek(sum)
                .cancelOrderRate(orderrate)
                .orderSuccessAmount(orderSuccess)
                .orderThroughHour(orderthroughhour)
                .top3Productdts(top3product)
                .build();
        return statiticsbyWeek;
    }

    @Override
    public StatiticsbyMonth getMonthRevenue() {
        LocalDateTime firstDateTimeOfMonth = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        LocalDateTime timenow = LocalDateTime.now();
        List<Order> orders= orderRepos.findByOrderDateBetween(firstDateTimeOfMonth,timenow);
        List<Top3Userdt> top3Userdts = orderRepos.findTop3User(firstDateTimeOfMonth,timenow);
        List<User> newusers = userser.findnewusers(firstDateTimeOfMonth,timenow);
        long orderSuccess =0;
        long orderCancel =0;
        double orderrate ;
        long sum=0;
        for(Order order: orders){
            if(order.getOrderstatus()==3) {
                sum += order.getTotal();
                orderSuccess++;
            }
            else if(order.getOrderstatus()==4) orderCancel ++;

        }
        orderrate = (double)orderCancel/(orderCancel+orderSuccess) * 100;
        StatiticsbyMonth statiticsbyMonth = StatiticsbyMonth.builder()
                .revenuebyMonth(sum)
                .cancelOrderRate(orderrate)
                .orderSuccessAmount(orderSuccess)
                .newusers(newusers)
                .top3Userdts(top3Userdts)
                .build();
        return statiticsbyMonth;
    }

    @Override
    public Integer getPendingOrderamount() {
        return orderRepos.findPendingOrders().size();
    }

}
