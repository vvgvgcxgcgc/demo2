package com.example.demo.CUSTOMER;
import com.example.demo.Domain.User;
import com.example.demo.Service.Userser;
import com.example.demo.Service.Userserimp;
import com.example.demo.dto.Userdt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static java.lang.Math.log;

@Controller
@RequiredArgsConstructor
@Log

public class CustomerController {
    private final Userser userser;
    @GetMapping("/admin-products")
    public String showAdminProducts(Model model){
        return "admin-products";
    }
    @GetMapping("/contact")
    public String showContact(Model model){
        return "contact";
    }

    @GetMapping("/shoping-cart")
    public String showShoppingCart(Model model){
        return "shoping-cart";
    }

    @GetMapping("/shop-grid")
    public String showShopGrid(Model model){
        return "shop-grid";
    }

    @GetMapping("/homepage")
    public String showHomePage(Model model){
        return "homepage";
    }

    @GetMapping("/shop-details")
    public String showShopDetails(Model model){
        return "shop-details";
    }

    @GetMapping("/checkout")
    public String showCheckOut(Model model){
        return "checkout";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        Userdt userdt = Userdt.builder().build();
        model.addAttribute("userdt",userdt);
        return "register"; // Đây là tên của file HTML Thymeleaf (không cần phần mở rộng .html)
    }
    @PostMapping("/register-new")
    public String addNewAdmin(@Valid @ModelAttribute("userdt") Userdt userdt,
                              BindingResult result,
                              Model model) {
        log.info(userdt.getAddress());
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
    @PostMapping("/login")
    public String Vertify(@Valid @ModelAttribute("userdt") Userdt userdt,
                          BindingResult result,
                          Model model ){
        if(result.hasErrors()){
            model.addAttribute("userdt",userdt);
            return "register";
        }
        String username = userdt.getUsername();
        User user = userser.findByUsername(username);
        if(user == null){
            model.addAttribute("userdt",userdt);
            model.addAttribute("ErrorPass","username is not registered");
            return "login";
        }
        return "redirect:/home";


    }


}

