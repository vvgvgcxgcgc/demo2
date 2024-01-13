package com.example.demo.ADMIN;

import com.example.demo.Domain.Product;
import com.example.demo.Domain.User;
import com.example.demo.Service.OrderService;
import com.example.demo.Service.ProductService;
import com.example.demo.dto.Orderdt;
import com.example.demo.dto.Productdt;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log
public class ProductController {
    private final ProductService productService;
    private final OrderService orderService;

    @GetMapping("/admin-dashboard")
    public String viewStatistic(Model model){
        Long earningToday = orderService.getDayRevenue().getRevenuebyDay();
        Long earningThisMonth = orderService.getMonthRevenue().getRevenuebyMonth();
        Long completedOrdersToday = orderService.getDayRevenue().getOrderSuccessAmount();
        Long completedOrdersThisMonth = orderService.getMonthRevenue().getOrderSuccessAmount();
        Integer pendingOrders = orderService.getPendingOrderamount();
        Long cancelRate = Math.round(orderService.getWeekRevenue().getCancelOrderRate());
        List<User> newMembers = orderService.getMonthRevenue().getNewusers();
        List<Integer> ordersInAnHour = orderService.getWeekRevenue().getOrderThroughHour();

        model.addAttribute("earningToday",earningToday);
        model.addAttribute("earningThisMonth",earningThisMonth);
        model.addAttribute("completedToday",completedOrdersToday);
        model.addAttribute("completedThisMonth",completedOrdersThisMonth);
        model.addAttribute("pendingOrders",pendingOrders);
        model.addAttribute("cancelRate",cancelRate);
        model.addAttribute("newMembers",newMembers.size());
        model.addAttribute("ordersInAnHour", ordersInAnHour);

        if(Orderdt.countOrder>0){
            model.addAttribute("newOrderNum", Orderdt.countOrder);
            model.addAttribute("notiTime", LocalDateTime.now());
            Orderdt.countOrder =0;
        }

        return "admin-dashboard";
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
        model.addAttribute("newOrderNum", 0);
        model.addAttribute("notiTime", 0);

        return "admin-products";
    }

    @GetMapping("/admin-add-product")
    public String addProductPage(Model model) {

        model.addAttribute("title", "Add Product");
        model.addAttribute("productDt", new Productdt());
        model.addAttribute("newOrderNum", 0);
        model.addAttribute("notiTime", 0);

        return "admin-add-product";
    }
    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("productDt") Productdt product,
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
    public String deleteProduct(String id, RedirectAttributes redirectAttributes) {
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
    public String enableProduct(String id, RedirectAttributes redirectAttributes) {
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
        model.addAttribute("newOrderNum", 0);
        model.addAttribute("notiTime", 0);

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
}