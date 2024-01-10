package com.example.demo.ADMIN;

import com.example.demo.Domain.Feedback;
import com.example.demo.Domain.Order;
import com.example.demo.Domain.Product;
import com.example.demo.Domain.User;
import com.example.demo.Service.FeedbackService;
import com.example.demo.Service.OrderService;
import com.example.demo.Service.ProductService;
import com.example.demo.dto.Feedbackdt;
import com.example.demo.dto.Productdt;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log
public class AdminController {
    private final ProductService productService;
    private final OrderService orderService;
    private final FeedbackService feedbackService;

//    @GetMapping("/admin-orders-pending")
//    public String showAdminOrdersPending(Model model){
//        return "admin-orders-pending";
//    }
@GetMapping("/admin-dashboard")
public String viewStatistic(Model model){
    Long earningToday = orderService.getDayRevenue().getRevenuebyDay();
    Long earningThisMonth = orderService.getMonthRevenue().getRevenuebyMonth();
    Long completedOrdersToday = orderService.getDayRevenue().getOrderSuccessAmount();
    Long completedOrdersThisMonth = orderService.getMonthRevenue().getOrderSuccessAmount();
    Integer pendingOrders = orderService.getPendingOrderamount();
    Double cancelRate = orderService.getWeekRevenue().getCancelOrderRate();
    List<User> newMembers = orderService.getMonthRevenue().getNewusers();

    model.addAttribute("earningToday",earningToday);
    model.addAttribute("earningThisMonth",earningThisMonth);
    model.addAttribute("completedToday",completedOrdersToday);
    model.addAttribute("completedThisMonth",completedOrdersThisMonth);
    model.addAttribute("pendingOrders",pendingOrders);
    model.addAttribute("cancelRate",cancelRate);
    model.addAttribute("newMembers",newMembers.size());
    return "admin-dashboard";
}

@GetMapping("/admin-orders-pending")
public String viewOrderpending(Model model){
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

    model.addAttribute("orders",orders);
    return "/admin-orders-pending";

}
    @GetMapping("/admin-orders-completed")
    public String showAdminOrdersCompleted(Model model){
        List<Order> orders = orderService.getAllOrders();

        model.addAttribute("orders",orders);

    return "admin-orders-completed";
    }

    @GetMapping("/admin-products")
    public String showAdminProducts(Model model ,Principal principal){
        if (principal == null) {
            return "redirect:/login";
        }

        List<Product> products = productService.getAllProducts();
        List<Productdt> productdts = new ArrayList<>();
        for(Product p:products ){
            Productdt productdt = Productdt.builder()
                                            .id(p.getId())
                                            .name(p.getName())
                                            .price(p.getPrice())
                                            .info(p.getInfo())
                                            .deleted(p.getDeleted())
                                            .image(p.getImage())
                                            .build();
            productdts.add(productdt);
        }
        model.addAttribute("products", productdts);
        model.addAttribute("size", productdts.size());
        return "admin-products";
    }

    @GetMapping("/admin-add-product")
    public String addProductPage(Model model) {

        model.addAttribute("title", "Add Product");

        model.addAttribute("productDt", new Productdt());
        return "admin-add-product";
    }
    @PostMapping("/save-product")
    public String Saveproduct(@ModelAttribute("productDt") Productdt product,
                            @RequestParam("imageProduct") MultipartFile imageProduct,
                             RedirectAttributes redirectAttributes){
        try {
            System.out.println(product.getId());
            
            productService.save(imageProduct, product);
            redirectAttributes.addFlashAttribute("success", "Add new product successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add new product!");
        }
        return "redirect:/admin-products";

    }
    @RequestMapping(value = "/delete-product", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deletedProduct(String id, RedirectAttributes redirectAttributes) {
        try {

            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Deleted failed!");
        }
        return "redirect:/admin-products";
    }
    @RequestMapping(value = "/enable-product", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enabledProduct(String id, RedirectAttributes redirectAttributes) {
        try {

            productService.enableById(id);
            redirectAttributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Enabled failed!");
        }
        return "redirect:/admin-products";
    }
    @GetMapping("/admin-update-product/{id}")
    public String updateProductForm(@PathVariable("id") String id, Model model) {

        Productdt productdt = productService.getById(id);
        model.addAttribute("title", "UPDATE Product");

        model.addAttribute("productDt", productdt);
        return "admin-update-product";
    }
    @PostMapping("/admin-update-product/{id}")
    public String updateProduct(@ModelAttribute("productDt") Productdt productdt,
                                @RequestParam("imageProduct") MultipartFile imageProduct,
                                RedirectAttributes redirectAttributes) {
        try {

            productService.update(imageProduct, productdt);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/admin-products";
    }
    @RequestMapping(value = "/accept-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String acceptorder(Long id, RedirectAttributes redirectAttributes) {
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
    public String cancelorder(Long id, RedirectAttributes redirectAttributes) {
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
    public String completeorder(Long id, RedirectAttributes redirectAttributes) {
        try {

            Order order = orderService.getSuccessOrder(id);

            redirectAttributes.addFlashAttribute("success", "Completed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Completed failed!");
        }
        return "redirect:/admin-orders-pending";
    }

    @GetMapping("/admin-feedbacks")
    public String viewFb(Model model){
        List<Feedbackdt> feedbackdts = feedbackService.getALLFb();
        model.addAttribute("feedbacks",feedbackdts);
        return "/admin-feedbacks";

    }
    @RequestMapping(value = "/accept-feedback", method = {RequestMethod.PUT, RequestMethod.GET})
    public String acceptFb(Long id){
        Feedback feedback = feedbackService.updateOrder(id);

        return "redirect:/admin-feedbacks";
    }
    @RequestMapping(value = "/delete-feedback", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deleteFb(Long id){
        feedbackService.deleteFB(id);
        return "redirect:/admin-feedbacks";
    }





}
