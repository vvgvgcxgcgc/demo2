package com.example.demo.CUSTOMER;
import com.example.demo.Domain.*;
import com.example.demo.Service.*;
import com.example.demo.dto.Orderdt;
import com.example.demo.dto.Userdt;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Log

public class CartOrderController {
    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;
    @GetMapping("/checkout")
    public String showCheckout(Model model, Principal principal){
        if(principal== null) model.addAttribute("display",true);
        else{

            model.addAttribute("display",false);
            model.addAttribute("checkadmin",true);
        }

        model.addAttribute("order",new Orderdt());
        return "user-checkout";
    }

    @GetMapping("/checkoutREG")
    public String showCheckOut(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName());
        user = userService.updateVoucher(user);
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
                .vouchers(user.getVoucherList())
                .build();

        List<Voucher> vouchersL = customer.getVouchers();
        System.out.println("===========================================Vouchers Des=================================================");
        for (Voucher voucher : vouchersL) {
            System.out.println(voucher.getDescription());
        }
        System.out.println("===========================================Vouchers Des=================================================");

        model.addAttribute("vouchers", vouchersL);
        model.addAttribute("user", customer);
        model.addAttribute("order",new Orderdt());
        return "user-checkoutREG";
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
            User user = userService.findByUsername(principal.getName());
            model.addAttribute("userFullname", user.getFullname());
            model.addAttribute("display",false);
            model.addAttribute("checkadmin",false);
            model.addAttribute("displayElement",true);
        }

        System.out.println("RECEIVE: " + model.getAttribute("delProductsId"));


        return "user-shopping-cart";
    }

    @PostMapping("/checkExistProductInCart")
    public String checkExistProductInCart(@RequestParam("productsId") List<String> productsId,
                                          RedirectAttributes redirectAttributes,
                                          Principal principal) {
        List<String> delProductsId = new ArrayList<>();
        for (String s : productsId) {
            Product product = productService.getProductById(s);
            if(product.getDeleted()) delProductsId.add(s);
        }

        if (delProductsId.isEmpty()) {
            System.out.println("CHECKOUTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
            if(principal== null || principal.getName().equals("adminonly")) {
                return "redirect:/checkout";
            } else {
                return "redirect:/checkoutREG";
            }
        } else {
            System.out.println("BACK TO CARTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
            redirectAttributes.addFlashAttribute("error", "error");
            redirectAttributes.addFlashAttribute("delProductsId", delProductsId);
            System.out.println("delProductsId: " + delProductsId);
            return "redirect:/shoping-cart";
        }
    }

    @PostMapping("/place-order")
    public String placeOrder(@ModelAttribute("order") Orderdt orderdt, @RequestParam("input_id") List<String> productlist,
                             RedirectAttributes redirectAttributes,
                             @RequestParam("input_quantity")List<Integer> quantitylist,Principal principal){
        if(principal== null || principal.getName().equals("adminonly")) {
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
                                @RequestParam("input_quantity")List<Integer> quantitylist,Principal principal,@RequestParam("voucherID") Long check){

        Order order = orderService.save1(orderdt,principal.getName());
        for (int i = 0; i < productlist.size(); i++) {
            orderService.save_productOrder(order.getId(), productlist.get(i), quantitylist.get(i));
        }
        User user = userService.updateAddress(principal.getName(),orderdt.getAddress());
        if(check>0)
             user = userService.deleteVoucherUser(user,check);
        int point = orderdt.getTotalPrice()/1000;
        user = userService.updateUserPoint(user,point);
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