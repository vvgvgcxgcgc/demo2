package com.example.demo.CUSTOMER;
import com.example.demo.Domain.Feedback;
import com.example.demo.Domain.Order;
import com.example.demo.Domain.Product;
import com.example.demo.Domain.User;
import com.example.demo.Service.*;
import com.example.demo.dto.Orderdt;
import com.example.demo.dto.Productdt;
import com.example.demo.dto.Userdt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

public class CustomerController {
    private final Userser userser;
    private final ProductService productService;
    private final OrderService orderService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final FeedbackService feedbackService;
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


    @GetMapping("/forgot-password")
    public String showForgotPassword(Model model){
        return "forgot-password";
    }
    @GetMapping("/reset-password")
    public String showResetPassword(Model model){
        return "reset-password";
    }
//    @GetMapping("/admin-orders")
//    public String showAdminOrders(Model model){
//        return "admin-orders";
//    }
    @GetMapping("/contact")
    public String showContact(Model model){
        return "contact";
    }

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
                    .feedBackList(p.getFb())
                    .image(p.getImage())
                    .build();
            productdts.add(productdt);
        }
        model.addAttribute("products", productdts);
        return "homepage";

    }
    @PostMapping("/add-feedback")
    public String addFb(String product_id,@RequestParam("feedbackMessage") String fb, Principal principal, RedirectAttributes redirectAttributes){
        Product product = productService.getProductById(product_id);
        User user;
        if(principal!=null && !principal.getName().equals("adminonly")){
            user=userser.findByUsername(principal.getName());
            Feedback feedback = Feedback.builder()
                    .time(LocalDateTime.now())
                    .product(product)
                    .user(user)
                    .message(fb)
                    .status(0)
                    .build();
            feedbackService.save(feedback);
        }
        else {
            return "redirect:/login";
        }


        redirectAttributes.addFlashAttribute("success","Thank you for giving feedback");
        return "redirect:/homepage";


    }

    @GetMapping("/my-account")
    public String profile(Model model, Principal principal) {

        if(principal == null||principal.getName().equals("adminonly")) {
            return "redirect:/login";
        }
        System.out.println(orderService.getDayRevenue());


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
                .orders(user.getOrders())
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
                             RedirectAttributes redirectAttributes,
                             @RequestParam("input_quantity")List<Integer> quantitylist,Principal principal){
       if(principal== null || principal.getName() == "adminonly") {
           Order order = orderService.save(orderdt);
           for (int i = 0; i < productlist.size(); i++) {
               orderService.save_productOrder(order.getId(), productlist.get(i), quantitylist.get(i));
           }
       }
       redirectAttributes.addFlashAttribute("successOrder", "Order placed successfully!");
       return "redirect:/homepage";
    }
    @PostMapping("/place-orderREG")
    public String placeorderREG(@ModelAttribute("order") Orderdt orderdt, @RequestParam("input_id") List<String> productlist,
                                RedirectAttributes redirectAttributes,
                                @RequestParam("input_quantity")List<Integer> quantitylist,Principal principal){

        Order order = orderService.save1(orderdt,principal.getName());
        for (int i = 0; i < productlist.size(); i++) {
            orderService.save_productOrder(order.getId(), productlist.get(i), quantitylist.get(i));
        }

        redirectAttributes.addFlashAttribute("successOrderREG", "Order placed successfully!");
        return "redirect:/homepage";
    }
    @RequestMapping(value = "/cancel-order-user", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelorder(Long id, RedirectAttributes redirectAttributes) {
        try {

            Order order = orderService.cancelOrder(id);

            redirectAttributes.addFlashAttribute("success", "Cancelled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Cancelled failed!");
        }
        return "redirect:/my-account";
    }


}

