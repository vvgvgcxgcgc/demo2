package com.example.demo.CUSTOMER;
import com.example.demo.Domain.Product;
import com.example.demo.Domain.User;
import com.example.demo.Service.ProductService;
import com.example.demo.Service.Userser;
import com.example.demo.Service.Userserimp;
import com.example.demo.dto.Productdt;
import com.example.demo.dto.Userdt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import org.springframework.boot.Banner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import static java.lang.Math.log;

@Controller
@RequiredArgsConstructor
@Log

public class CustomerController {
    private final Userser userser;
    private final ProductService productService;
    private final BCryptPasswordEncoder passwordEncoder;
    @GetMapping("/checkout")
    public String showCheckout(Model model){
        return "checkout";
    }
//    @GetMapping("/admin-add-product")
//    public String showAdminAddProduct(Model model){
//        return "admin-add-product";
//    }

//    @GetMapping("/admin-categories")
//    public String showAdminCategories(Model model){
//        return "admin-categories";
//    }
//
//    @GetMapping("/admin-forgot-password")
//    public String showAdminForgotPassword(Model model){
//        return "admin-forgot-password";
//    }
//    @GetMapping("/admin-orders")
//    public String showAdminOrders(Model model){
//        return "admin-orders";
//    }
//
//    @GetMapping("/admin-product-result")
//    public String showAdminProductResult(Model model){
//        return "admin-product-result";
//    }
////    @GetMapping("/admin-products")
////    public String showAdminProducts(Model model){
////        return "admin-products";
////    }
//
//    @GetMapping("/admin-update-product")
//    public String showAdminUpdateProduct(Model model){
//        return "admin-update-product";
//    }
//    @GetMapping("/admin-dashboard")
//    public String showAdminDashboard(Model model){
//        return "admin-dashboard";
//    }
//
//    @GetMapping("/contact")
//    public String showContact(Model model){
//        return "contact";
//    }
//
//    @GetMapping("/shoping-cart")
//    public String showShoppingCart(Model model){
//        return "shoping-cart";
//    }
//
//    @GetMapping("/shop-grid")
//    public String showShopGrid(Model model){
//        return "shop-grid";
//    }
//
//    @GetMapping("/homepage")
//    public String showHomePage(Model model){
//        return "homepage";
//    }
//
//    @GetMapping("/shop-details")
//    public String showShopDetails(Model model){
//        return "shop-details";
//    }
//

    @GetMapping("/checkoutREG")
    public String showCheckOut(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        User user = userser.findByUsername(username);

        Userdt customer = Userdt.builder()
                .Username(user.getUsername())
                .addresses(user.getAddresses())
                .avatar(user.getAvatar())
                .Fullname(user.getFullname())
                .id(user.getId())
                .Userpoint(user.getUserpoint())
                .Phonenumber(user.getPhonenumber())
                .build();
        model.addAttribute("user", customer);

        return "checkoutREG";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        Userdt userdt = Userdt.builder().build();

        model.addAttribute("userdt",userdt);
        return "register"; // Đây là tên của file HTML Thymeleaf (không cần phần mở rộng .html)
    }
    @PostMapping("/register-new")
    public String addNewAdmin(@Valid @ModelAttribute("userdt") Userdt userdt,@RequestParam("address") String address,
                              BindingResult result,
                              Model model) {
        if(result.hasErrors()){
          model.addAttribute("userdt",userdt);
          return "register";
        }
        String username = userdt.getUsername();
        User user = userser.findByUsername(username);
        if(user != null){
            model.addAttribute("userdt",userdt);
            model.addAttribute("usernameErr","username has existed");
            log.info("username has existed");
            return "register";
        }
        if(userdt.getPassword().equals(userdt.getRepeatPassword())){
            userdt.setPassword(passwordEncoder.encode(userdt.getPassword()));
            userdt.setAddresses(new ArrayList<String>());
            userdt.getAddresses().add(address);
            userser.save(userdt);
            model.addAttribute("success", "Register successfully!");

        }
        else{
            model.addAttribute("userdt",userdt);
            model.addAttribute("passwordError","Your password maybe wrong! Check again!");
        }
        return "register";

    }
    @GetMapping("/login")
    public String ShowLogin(Model model){
        Userdt userdt = Userdt.builder().build();
        model.addAttribute("userdt",userdt);
        return "login";
    }
//    @PostMapping("/login")
//    public String Vertify(@Valid @ModelAttribute("userdt") Userdt userdt,
//                          BindingResult result,
//                          Model model ){
//        if(result.hasErrors()){
//            model.addAttribute("userdt",userdt);
//            return "register";
//        }
//        String username = userdt.getUsername();
//        User user = userser.findByUsername(username);
//        if(user == null){
//            model.addAttribute("userdt",userdt);
//            model.addAttribute("ErrorPass","username is not registered");
//            return "login";
//        }
//        return "redirect:/home";
//
//    }
    @GetMapping("/homepage")
    public String Viewhomepage(Model model){
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
        return "homepage";

    }
    @GetMapping("/my-account")
    public String profile(Model model, Principal principal) {
        if(principal == null||principal.getName().equals("adminonly")) {
            return "redirect:/login";
        }
        String username = principal.getName();
        User user = userser.findByUsername(username);

        Userdt customer = Userdt.builder()
                .Username(user.getUsername())
                .addresses(user.getAddresses())
                .avatar(user.getAvatar())
                .Fullname(user.getFullname())
                .id(user.getId())
                .Userpoint(user.getUserpoint())
                .Phonenumber(user.getPhonenumber())
                .build();
        model.addAttribute("user", customer);
        return "my-account";

    }
    @PostMapping("/update-profile")
    public String updateprofile( @ModelAttribute("user") Userdt userdt,Principal principal,
                                @RequestParam("imageUser") MultipartFile imageUser,RedirectAttributes redirectAttributes){
        if(principal == null||principal.getName().equals("adminonly")) {
            return "redirect:/login";
        }

        try {
             userdt.setUsername(principal.getName());
            userser.Update(imageUser, userdt);

            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/my-account";

    }
    @GetMapping("/shoping-cart")
    public String viewShopingcart(Principal principal){
        if(principal == null||principal.getName().equals("adminonly")) {
            return "0";
        }
        else return "1";


    }





}

