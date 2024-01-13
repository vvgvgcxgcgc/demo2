package com.example.demo.CUSTOMER;
import com.example.demo.Domain.*;
import com.example.demo.Service.*;
import com.example.demo.dto.Productdt;
import com.example.demo.dto.Top3Productdt;
import com.example.demo.dto.Userdt;
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
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log

public class CustomerController {
    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final FeedbackService feedbackService;
    private final DefaultAddressService defaultAddressService;

    @GetMapping("/contact")
    public String showContact(Model model, Principal principal){
        if(principal== null) {model.addAttribute("display",true);}
        else{
            model.addAttribute("display",false);
            if(principal.getName().equals("adminonly"))
                model.addAttribute("checkadmin",true);
            else {
                User user = userService.findByUsername(principal.getName());
                model.addAttribute("userFullname", user.getFullname());
                model.addAttribute("checkadmin", false);
            }
        }
        return "contact";
    }

    @GetMapping("/homepage")
    public String ViewHomepage(Model model, Principal principal){
        if(principal== null) {model.addAttribute("display",true);}
        else{
            model.addAttribute("display",false);
            if(principal.getName().equals("adminonly"))
                model.addAttribute("checkadmin",true);
            else {
                User user = userService.findByUsername(principal.getName());
                model.addAttribute("userFullname", user.getFullname());
                model.addAttribute("checkadmin", false);
            }
        }
        if(defaultAddressService.getAllAddress().isEmpty()) {
            defaultAddressService.save("Ngõ 8, Quận 6, Thành phố Hồ Chí Minh, Việt Nam");
            defaultAddressService.save("49, Phố Viên, Phường Cổ Nhuế 2, Quận Bắc Từ Liêm, Hà Nội, 11909, Việt Nam");
            defaultAddressService.save("99, 175/28/9, a, Phường Tăng Nhơn Phú A, Thành phố Thủ Đức, Thành phố Hồ Chí Minh, 71211, Việt Nam");
            defaultAddressService.save("11, Đường Nghiêm Xuân Yêm, Phường Đại Kim, Quận Hoàng Mai, Hà Nội, 10135, Việt Nam");
            defaultAddressService.save("7, Đường Lý Thường Kiệt, Phường Bắc Lý, Đồng Hới, Tỉnh Quảng Bình, Việt Nam");
            defaultAddressService.save("12, Quốc lộ 1, Phước Dân, Huyện Ninh Phước, Tỉnh Ninh Thuận, Việt Nam");
            defaultAddressService.save("16A, Phú Châu, Phường An Bình, Thành phố Thủ Đức, Thành phố Hồ Chí Minh, Dĩ An, Tỉnh Bình Dương, 00848, Việt Nam");
            defaultAddressService.save("52, Đường 30 Tháng 4, Phường Trung Dũng, Thành phố Biên Hòa, Tỉnh Đồng Nai, 76118, Việt Nam");
            defaultAddressService.save("6, Đường Nguyễn Công Trứ, Phường An Hải Bắc, Quận Sơn Trà, Thành phố Đà Nẵng, 02363, Việt Nam");
            defaultAddressService.save("17A, Mạc Thiên Tích, Phường Xuân Khánh, Ninh Kiều, Quận Ninh Kiều, Thành phố Cần Thơ, 94111, Việt Nam");
            defaultAddressService.save("15, Tiên Bình, Tác Tình, Thị trấn Tam Đường, Tỉnh Lai Châu, Việt Nam");
            defaultAddressService.save("55, P. Thành Tô, Phường Thành Tô, Quận Hải An, Hải Phòng, 04813, Việt Nam");
            defaultAddressService.save("90B, Đường tỉnh 619, Núi Thành, Tỉnh Quảng Nam, Việt Nam");
            defaultAddressService.save("Ngõ 3, Yên Cư, Đại Yên, Thành phố Hạ Long, Tỉnh Quảng Ninh, Việt Nam");
            defaultAddressService.save("3, Đường Trần Hải Thành, Phường Thuận An, Thành phố Huế, Phú Vang, Thừa Thiên Huế, Việt Nam");
            defaultAddressService.save("44A1 Đường Ngụy Khắc Tuần, Xuân An, Nghi Xuân, Hà Tĩnh, 45506, Việt Nam");
            defaultAddressService.save("6A, Đường Sông Xoài-Cù Bị, Sông Xoài 2, Xã Sông Xoài, Thị xã Phú Mỹ, Tỉnh Bà Rịa - Vũng Tàu, Việt Nam");
            defaultAddressService.save("16, Nguyễn Thị Bảy, Thị trấn Cần Giuộc, Huyện Cần Giuộc, Tỉnh Long An, Việt Nam");
            defaultAddressService.save("9A, P. Hà Huy Tập, Thành phố Vinh, Tỉnh Nghệ An, Việt Nam");
            defaultAddressService.save("23, Bào Toàn, Bảo Lạc, Tỉnh Cao Bằng, Việt Nam");
            defaultAddressService.save("8, Ngõ 115 Trần Cung, Phường Nghĩa Tân, Quận Cầu Giấy, Hà Nội, 10065, Việt Nam");
        }

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
        List<String> s = productService.getAllProductName();
        model.addAttribute("suggestions",s);
        model.addAttribute("menuTitle","Today's Menu");

        return "homepage";
    }

    @PostMapping("/add-feedback")
    public String addFb(String product_id,@RequestParam("feedbackMessage") String fb, Principal principal, RedirectAttributes redirectAttributes){
        Product product = productService.getProductById(product_id);
        User user;
        if(principal!=null && !principal.getName().equals("adminonly")){
            user= userService.findByUsername(principal.getName());
            Feedback feedback = Feedback.builder()
                    .time(LocalDateTime.now())
                    .product(product)
                    .user(user)
                    .message(fb)
                    .status(0)
                    .build();
            feedbackService.save(feedback);
        } else {
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

        User user = userService.findByUsername(principal.getName());
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
    public String updateProfile( @ModelAttribute("user") Userdt userdt,Principal principal,
                                @RequestParam("selectedAddr") String selectedAddr,
                                @RequestParam("imageUser") MultipartFile imageUser,RedirectAttributes redirectAttributes){
        if(principal == null||principal.getName().equals("adminonly")) {
            return "redirect:/login";
        }
        try {
             userdt.setUsername(principal.getName());
            userService.Update(imageUser, userdt);
            userService.updateFirstAddress(principal.getName(),selectedAddr);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/my-account";
    }

    @PostMapping("/searchProduct")
    public String searchProduct(@RequestParam("prodName") String prodName,
                                Model model,Principal principal){
        if(principal== null) {model.addAttribute("display",true);}
        else{
            model.addAttribute("display",false);
            if(principal.getName().equals("adminonly"))
                model.addAttribute("checkadmin",true);
            else {
                User user = userService.findByUsername(principal.getName());
                model.addAttribute("userFullname", user.getFullname());
                model.addAttribute("checkadmin", false);
            }
        }

        List<Product> products = productService.getProductByName(prodName);
        List<Product> allProducts = productService.getAllProducts();
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
        }
        for (Product p : allProducts){
            Productdt productdt = Productdt.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .price(p.getPrice())
                    .info(p.getInfo())
                    .deleted(p.getDeleted())
                    .feedBackList(p.getFb())
                    .image(p.getImage())
                    .build();
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
        List<String> s = productService.getAllProductName();
        model.addAttribute("suggestions",s);
        model.addAttribute("menuTitle","Search Result");

        return "homepage";
    }
}

