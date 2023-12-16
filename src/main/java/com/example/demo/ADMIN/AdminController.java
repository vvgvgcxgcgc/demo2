package com.example.demo.ADMIN;

import com.example.demo.Domain.Product;
import com.example.demo.Service.ProductService;
import com.example.demo.dto.Productdt;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log
public class AdminController {
    private final ProductService productService;
    @GetMapping("/view_product")
    public String viewProduct(Model model){
        List<Product> products = productService.getAllProducts();
        List<Productdt> productdts = new ArrayList<Productdt>();
        for(Product p : products){
            Productdt productdt = Productdt.builder()
                                .id(p.getId())
                                .Name(p.getName())
                                .Price(p.getPrice())
                                .Info(p.getInfo())
                                .Image(p.getImage())
                                .fb(p.getFb())
                                .build();
            productdts.add(productdt);
        }
        model.addAttribute("productList",productdts);

        return "view_product";
    }

}
