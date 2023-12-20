package com.example.demo.ADMIN;

import com.example.demo.Domain.Product;
import com.example.demo.Service.ProductService;
import com.example.demo.dto.Productdt;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static java.rmi.server.LogStream.log;

@Controller
@RequiredArgsConstructor
@Log
public class AdminController {
    private final ProductService productService;
    @GetMapping("/admin-products")
    public String showAdminProducts(Model model){
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
        return "admin-products";
    }

    @GetMapping("/admin-add-product")
    public String addProductPage(Model model) {

        model.addAttribute("title", "Add Product");

        model.addAttribute("productDt", new Productdt());
        return "admin-add-product";
    }
    @PostMapping("/save-product")
    public String Saveproduct(@ModelAttribute("productDt") Productdt product,
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
    public String deletedProduct(String id, RedirectAttributes redirectAttributes) {
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
    public String enabledProduct(String id, RedirectAttributes redirectAttributes) {
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
