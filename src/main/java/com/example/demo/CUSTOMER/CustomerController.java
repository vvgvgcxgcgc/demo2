package com.example.demo.CUSTOMER;
import com.example.demo.Domain.*;
import com.example.demo.Service.*;
import com.example.demo.dto.Orderdt;
import com.example.demo.dto.Productdt;
import com.example.demo.dto.Top3Productdt;
import com.example.demo.dto.Userdt;
import com.sun.tools.jconsole.JConsoleContext;
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
import java.util.Collections;
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
    private final DefaultAddressService defaultAddressService;
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
       if( model.getAttribute("suggestAddr") == null){
           return "redirect:/login";
       }

        return "forgot-password";
    }
    @GetMapping("/reset-password")
    public String showResetPassword(Model model){
        if( model.getAttribute("usernameForgot") == null){
            return "redirect:/login";

        }
        return "reset-password";
    }
//    @GetMapping("/admin-orders")
//    public String showAdminOrders(Model model){
//        return "admin-orders";
//    }
    @GetMapping("/contact")
    public String showContact(Model model, Principal principal){
        if(principal== null) {model.addAttribute("display",true);}
        else{
            model.addAttribute("display",false);
            if(principal.getName().equals("adminonly"))
                model.addAttribute("checkadmin",true);
            else {
                User user = userser.findByUsername(principal.getName());
                model.addAttribute("userFullname", user.getFullname());
                model.addAttribute("checkadmin", false);
            }
        }

        return "contact";
    }

    @GetMapping("/checkoutREG")
    public String showCheckOut(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userser.findByUsername(principal.getName());
        model.addAttribute("userFullname", user.getFullname());
        model.addAttribute("display",false);
        model.addAttribute("checkadmin",false);


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



    @GetMapping("/homepage")
    public String Viewhomepage(Model model, Principal principal){
        if(principal== null) {model.addAttribute("display",true);}
        else{
            model.addAttribute("display",false);
            if(principal.getName().equals("adminonly"))
                model.addAttribute("checkadmin",true);
            else {
                User user = userser.findByUsername(principal.getName());
                model.addAttribute("userFullname", user.getFullname());
                model.addAttribute("checkadmin", false);
            }
        }

//        defaultAddressService.save("Ngõ 8, Quận 6, Thành phố Hồ Chí Minh, Việt Nam") ;
//        defaultAddressService.save("49, Phố Viên, Phường Cổ Nhuế 2, Quận Bắc Từ Liêm, Hà Nội, 11909, Việt Nam");
//        defaultAddressService.save("99, 175/28/9, a, Phường Tăng Nhơn Phú A, Thành phố Thủ Đức, Thành phố Hồ Chí Minh, 71211, Việt Nam");
//        defaultAddressService.save("11, Đường Nghiêm Xuân Yêm, Phường Đại Kim, Quận Hoàng Mai, Hà Nội, 10135, Việt Nam");
//        defaultAddressService.save("7, Đường Lý Thường Kiệt, Phường Bắc Lý, Đồng Hới, Tỉnh Quảng Bình, Việt Nam");
//        defaultAddressService.save("12, Quốc lộ 1, Phước Dân, Huyện Ninh Phước, Tỉnh Ninh Thuận, Việt Nam");
//        defaultAddressService.save("16A, Phú Châu, Phường An Bình, Thành phố Thủ Đức, Thành phố Hồ Chí Minh, Dĩ An, Tỉnh Bình Dương, 00848, Việt Nam");
//        defaultAddressService.save("52, Đường 30 Tháng 4, Phường Trung Dũng, Thành phố Biên Hòa, Tỉnh Đồng Nai, 76118, Việt Nam");
//        defaultAddressService.save("6, Đường Nguyễn Công Trứ, Phường An Hải Bắc, Quận Sơn Trà, Thành phố Đà Nẵng, 02363, Việt Nam");
//        defaultAddressService.save( "17A, Mạc Thiên Tích, Phường Xuân Khánh, Ninh Kiều, Quận Ninh Kiều, Thành phố Cần Thơ, 94111, Việt Nam");
//        defaultAddressService.save("15, Tiên Bình, Tác Tình, Thị trấn Tam Đường, Tỉnh Lai Châu, Việt Nam");
//        defaultAddressService.save("55, P. Thành Tô, Phường Thành Tô, Quận Hải An, Hải Phòng, 04813, Việt Nam");
//        defaultAddressService.save("90B, Đường tỉnh 619, Núi Thành, Tỉnh Quảng Nam, Việt Nam");
//        defaultAddressService.save("Ngõ 3, Yên Cư, Đại Yên, Thành phố Hạ Long, Tỉnh Quảng Ninh, Việt Nam");
//        defaultAddressService.save("3, Đường Trần Hải Thành, Phường Thuận An, Thành phố Huế, Phú Vang, Thừa Thiên Huế, Việt Nam");
//        defaultAddressService.save("44A1 Đường Ngụy Khắc Tuần, Xuân An, Nghi Xuân, Hà Tĩnh, 45506, Việt Nam");
//        defaultAddressService.save("6A, Đường Sông Xoài-Cù Bị, Sông Xoài 2, Xã Sông Xoài, Thị xã Phú Mỹ, Tỉnh Bà Rịa - Vũng Tàu, Việt Nam");
//        defaultAddressService.save("16, Nguyễn Thị Bảy, Thị trấn Cần Giuộc, Huyện Cần Giuộc, Tỉnh Long An, Việt Nam");
//        defaultAddressService.save("9A, P. Hà Huy Tập, Thành phố Vinh, Tỉnh Nghệ An, Việt Nam");
//        defaultAddressService.save("23, Bào Toàn, Bảo Lạc, Tỉnh Cao Bằng, Việt Nam");
//        defaultAddressService.save("8, Ngõ 115 Trần Cung, Phường Nghĩa Tân, Quận Cầu Giấy, Hà Nội, 10065, Việt Nam");

        List<Product> products = productService.getAllProducts();
        List<Productdt> productdts = new ArrayList<>();
        List<Productdt> reverseProducts = new ArrayList<>();
        for (Product p : products){
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
            reverseProducts.add(productdt);
        }
        List<Top3Productdt> top3Products = orderService.getWeekRevenue().getTop3Productdts();
        Collections.reverse(reverseProducts);
        List<Productdt> page1Products;
        List<Productdt> page2Products;
        if (reverseProducts.size() >= 6){
            page1Products = reverseProducts.subList(0,3);
            page2Products = reverseProducts.subList(3,6);
        } else if (reverseProducts.size() <= 3) {
            page1Products = reverseProducts;
            page2Products = reverseProducts;
        } else {
            page1Products = reverseProducts.subList(0,3);
            page2Products = reverseProducts.subList(3,reverseProducts.size());
        }

        List<Product> topFeedback = productService.getTop6Productfeedback();
        List<Product> page1Feedback;
        List<Product> page2Feedback;
        if (topFeedback.size() <= 3){
            page1Feedback = topFeedback;
            page2Feedback = topFeedback;
        } else {
            page1Feedback = topFeedback.subList(0,3);
            page2Feedback = topFeedback.subList(3,topFeedback.size());
        }


        model.addAttribute("products", productdts);
        model.addAttribute("top3Products",top3Products);
        model.addAttribute("page1", page1Products);
        model.addAttribute("page2", page2Products);
        model.addAttribute("page1Feedback", page1Feedback);
        model.addAttribute("page2Feedback", page2Feedback);
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

        User user = userser.findByUsername(principal.getName());
        model.addAttribute("userFullname", user.getFullname());
        model.addAttribute("display",false);

        model.addAttribute("checkadmin",false);



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
                                @RequestParam("selectedAddr") String selectedAddr,
                                @RequestParam("imageUser") MultipartFile imageUser,RedirectAttributes redirectAttributes){
        if(principal == null||principal.getName().equals("adminonly")) {
            return "redirect:/login";
        }

        try {
             userdt.setUsername(principal.getName());
            userser.Update(imageUser, userdt);
            userser.updateFirstAddress(principal.getName(),selectedAddr);

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
            User user = userser.findByUsername(principal.getName());
            model.addAttribute("userFullname", user.getFullname());
            model.addAttribute("display",false);
            model.addAttribute("checkadmin",false);
            model.addAttribute("displayElement",true);
        }

        return "shoping-cart";
    }

    @PostMapping("/forgotPass")
    public String forgotPass(@RequestParam("usernameForgot") String usernameForgot,
                             RedirectAttributes redirectAttributes) {
        User user = userser.findByUsername(usernameForgot);
        if (user == null) {
            redirectAttributes.addFlashAttribute("ErrorPass", "Invalid username");
            return "redirect:/login";
        } else {

            String suggestAddr = userser.generateSampleAddress(usernameForgot);

            String realAddr = user.getAddresses().get(0);
            List<Defaultaddress> defaultaddresses = defaultAddressService.getAllAddress();

            redirectAttributes.addFlashAttribute("defaultaddresses", defaultaddresses);
            redirectAttributes.addFlashAttribute("suggestAddr", suggestAddr);
            redirectAttributes.addFlashAttribute("realAddr", realAddr);
            redirectAttributes.addFlashAttribute("usernameForgot", usernameForgot);

            return "redirect:/forgot-password";
        }
    }
    @PostMapping("/checkForgotPass")
    public String checkForgotPass(@RequestParam("selectedAddr") String selectedAddr,
                                  @RequestParam("phonenumber") String phonenumber,
                                  @RequestParam("usernameForgot") String usernameForgot,
                                  RedirectAttributes redirectAttributes) {

       User user =userser.findByUsername(usernameForgot);
       if(user.getPhonenumber().equals(phonenumber)&&user.getAddresses().get(0).equals(selectedAddr)){
           redirectAttributes.addFlashAttribute("usernameForgot", usernameForgot);
           return "redirect:/reset-password";

        }
       else {
           String suggestAddr = userser.generateSampleAddress(usernameForgot);

           String realAddr = user.getAddresses().get(0);
           List<Defaultaddress> defaultaddresses = defaultAddressService.getAllAddress();

           redirectAttributes.addFlashAttribute("defaultaddresses", defaultaddresses);
           redirectAttributes.addFlashAttribute("suggestAddr", suggestAddr);
           redirectAttributes.addFlashAttribute("realAddr", realAddr);
           redirectAttributes.addFlashAttribute("usernameForgot", usernameForgot);
          redirectAttributes.addFlashAttribute("ErrorPass", "Invalid phone number and/or address");
           return "redirect:/forgot-password";

       }


    }


    @PostMapping("/updatePass")
    public String updatePass(@RequestParam("password") String newPass,
                             @RequestParam("usernameForgot") String usernameForgot) {

        userser.updatePassword(usernameForgot,passwordEncoder.encode(newPass));

        return "redirect: /login";
    }

    @PostMapping("/searchProduct")
    public String searchProduct(@RequestParam("prodName") String prodName,
                                Model model){

//        List<Product> searchedProducts =
//        model.addAttribute("searchedProduct", searchedProducts);

        return "/homepage";
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
        Orderdt.countOrder++;

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
        User user = userser.updateAddress(principal.getName(),orderdt.getAddress());
        Orderdt.countOrder++;

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

