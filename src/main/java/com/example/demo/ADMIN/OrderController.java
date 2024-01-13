package com.example.demo.ADMIN;

import com.example.demo.Domain.Order;
import com.example.demo.Service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/admin-orders-pending")
    public String viewOrderPending(Model model){
        List<Order> orders = orderService.getAllOrders();
        for(Order order: orders){
            if(order.getOrderstatus()==1 || order.getOrderstatus()==2){
                LocalDateTime startDateTime = order.getTime();
                // LocalDateTime kết thúc
                LocalDateTime endDateTime = LocalDateTime.now(); // LocalDateTime kết thúc là thời điểm hiện tại
                Duration duration = Duration.between(startDateTime, endDateTime);
                if(duration.toHours()>6){
                    order.setOrderstatus(4);
                    orderService.cancelOrder(order.getId());
                }
            }
        }

        model.addAttribute("newOrderNum", 0);
        model.addAttribute("notiTime", 0);
        model.addAttribute("orders",orders);
        return "/admin-orders-pending";
    }

    @GetMapping("/admin-orders-completed")
    public String showOrdersCompleted(Model model){
        List<Order> orders = orderService.getAllOrders();

        model.addAttribute("orders",orders);
        model.addAttribute("newOrderNum", 0);
        model.addAttribute("notiTime", 0);

        return "admin-orders-completed";
    }

    @RequestMapping(value = "/accept-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String acceptOrder(Long id, RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.acceptOrder(id);

            redirectAttributes.addFlashAttribute("success", "Accepted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Accepted failed!");
        }
        return "redirect:/admin-orders-pending";
    }
    @RequestMapping(value = "/cancel-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelOrder(Long id, RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.cancelOrder(id);

            redirectAttributes.addFlashAttribute("success", "Canceled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Canceled failed!");
        }
        return "redirect:/admin-orders-pending";
    }
    @RequestMapping(value = "/complete-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String completeOrder(Long id, RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.getSuccessOrder(id);

            redirectAttributes.addFlashAttribute("success", "Completed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Completed failed!");
        }
        return "redirect:/admin-orders-pending";
    }
}