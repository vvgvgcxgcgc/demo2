package com.example.demo.CUSTOMER;
import com.example.demo.Domain.Order;
import com.example.demo.Domain.Product;
import com.example.demo.Domain.User;
import com.example.demo.Service.OrderService;
import com.example.demo.Service.ProductService;
import com.example.demo.Service.Userser;
import com.example.demo.Service.Userserimp;
import com.example.demo.dto.Orderdt;
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
    private final OrderService orderService;
    private final BCryptPasswordEncoder passwordEncoder;
    @GetMapping("/checkout")
    public String showCheckout(Model model, Principal principal){
        if(principal== null) model.addAttribute("display",true);
        else{
            model.addAttribute("display",false);
            model.addAttribute("checkadmin",true);
        }
        model.addAttribute("order",new Orderdt());
        return "checkout";
    }


//    @GetMapping("/admin-forgot-password")
//    public String showAdminForgotPassword(Model model){
//        return "admin-forgot-password";
//    }
//    @GetMapping("/admin-orders")
//    public String showAdminOrders(Model model){
//        return "admin-orders";
//    }
//    @GetMapping("/contact")
//    public String showContact(Model model){
//        return "contact";
//    }

    @GetMapping("/checkoutREG")
    public String showCheckOut(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        model.addAttribute("display",false);
        model.addAttribute("checkadmin",false);
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

        model.addAttribute("order",new Orderdt());

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
    public String ShowLogin(Model model, Principal principal,RedirectAttributes redirectAttributes){
        if(principal== null) {model.addAttribute("display",true);
            Userdt userdt = Userdt.builder().build();
            model.addAttribute("userdt",userdt);

            return "login";}
        else{

            redirectAttributes.addFlashAttribute("error","You must log out first");
            return "redirect:/homepage";



        }

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
    public String Viewhomepage(Model model, Principal principal){
        if(principal== null) {model.addAttribute("display",true);}
        else{
            model.addAttribute("display",false);
            if(principal.getName().equals("adminonly"))
                model.addAttribute("checkadmin",true);
            else
                model.addAttribute("checkadmin",false);

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
        return "homepage";

    }
    @GetMapping("/my-account")
    public String profile(Model model, Principal principal) {

        if(principal == null||principal.getName().equals("adminonly")) {
            return "redirect:/login";
        }

        model.addAttribute("display",false);

        model.addAttribute("checkadmin",false);


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
    public String showShoppingCart(Model model, Principal principal){
        if(principal == null||principal.getName().equals("adminonly")) {
            if(principal== null) model.addAttribute("display",true);
            else {
                model.addAttribute("display",false);
                model.addAttribute("checkadmin",true);

            }
            model.addAttribute("displayElement",false);
        }
        else {
            model.addAttribute("display",false);
            model.addAttribute("checkadmin",false);
            model.addAttribute("displayElement",true);
        }

        return "shoping-cart";
    }
    @PostMapping("/place-order")
    public String placeorder(@ModelAttribute("order") Orderdt orderdt, @RequestParam("input_id") List<String> productlist,
                             @RequestParam("input_quantity")List<Integer> quantitylist){
       Order order= orderService.save(orderdt);
       System.out.println("------------------Product list size: " + productlist.size());
       for(int i=0; i<productlist.size(); i++){
           orderService.save_productOrder(order.getId(),productlist.get(i),quantitylist.get(i));
       }
       System.out.println("------------------Total: " + order.getTotal());

       return "redirect:/homepage";
    }
//    @PostMapping("/fetch")
//    public String handleDataCheckout(@RequestBody Orderdt checkoutData) {
//        // Xử lý dữ liệu được gửi từ client thông qua @RequestBody và CheckoutData
//        // Ví dụ: In ra thông tin nhận được từ client
//        System.out.println(checkoutData.getName());
//
//        // Thực hiện các xử lý khác với dữ liệu nhận được
//        return "redirect:/hompage";
//    }

}

