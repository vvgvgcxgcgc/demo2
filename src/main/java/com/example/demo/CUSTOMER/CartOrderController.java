package com.example.demo.CUSTOMER;
import com.example.demo.Domain.*;
import com.example.demo.Service.*;
import com.example.demo.dto.Orderdt;
import com.example.demo.dto.Userdt;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log

public class CartOrderController {
    private final Userser userser;
    private final OrderService orderService;
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

    @PostMapping("/place-order")
    public String placeOrder(@ModelAttribute("order") Orderdt orderdt, @RequestParam("input_id") List<String> productlist,
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
    public String placeOrderREG(@ModelAttribute("order") Orderdt orderdt, @RequestParam("input_id") List<String> productlist,
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
    public String cancelOrder(Long id, RedirectAttributes redirectAttributes) {
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