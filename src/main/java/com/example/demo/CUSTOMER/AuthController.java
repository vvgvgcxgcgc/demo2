package com.example.demo.CUSTOMER;
import com.example.demo.Domain.*;
import com.example.demo.Service.*;
import com.example.demo.dto.Userdt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log

public class AuthController {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final DefaultAddressService defaultAddressService;

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
        User user = userService.findByUsername(username);
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
            userService.save(userdt);
            model.addAttribute("success", "Register successfully!");

        } else {
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

    @PostMapping("/forgotPass")
    public String forgotPass(@RequestParam("usernameForgot") String usernameForgot,
                             RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(usernameForgot);
        if (user == null) {
            redirectAttributes.addFlashAttribute("ErrorPass", "Invalid username");
            return "redirect:/login";
        } else {

            String suggestAddr = userService.generateSampleAddress(usernameForgot);
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

        User user = userService.findByUsername(usernameForgot);
        if(user.getPhonenumber().equals(phonenumber)&&user.getAddresses().get(0).equals(selectedAddr)){
            redirectAttributes.addFlashAttribute("usernameForgot", usernameForgot);
            return "redirect:/reset-password";
        }
        else {
            String suggestAddr = userService.generateSampleAddress(usernameForgot);
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

        userService.updatePassword(usernameForgot,passwordEncoder.encode(newPass));
        return "redirect:/login";
    }
}