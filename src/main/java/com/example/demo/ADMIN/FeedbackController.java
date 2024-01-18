package com.example.demo.ADMIN;

import com.example.demo.Domain.User;
import com.example.demo.Service.UserService;
import com.example.demo.dto.Userdt;
import com.example.demo.Domain.Feedback;
import com.example.demo.Service.FeedbackService;
import com.example.demo.dto.Feedbackdt;

import com.example.demo.dto.Voucherdt;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final UserService userService;

    @GetMapping("/admin-feedbacks")
    public String viewFeedback(Model model){
        List<Feedbackdt> feedbackdts = feedbackService.getALLFb();

        model.addAttribute("feedbacks",feedbackdts);
        model.addAttribute("newOrderNum", 0);
        model.addAttribute("notiTime", 0);

        return "/admin-feedbacks";
    }

    @RequestMapping(value = "/accept-feedback", method = {RequestMethod.PUT, RequestMethod.GET})
    public String acceptFeedback(Long id){
        Feedback feedback = feedbackService.updateOrder(id);
        return "redirect:/admin-feedbacks";
    }

    @RequestMapping(value = "/delete-feedback", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deleteFeedback(Long id){
        feedbackService.deleteFB(id);
        return "redirect:/admin-feedbacks";
    }

    @GetMapping("/admin-add-voucher")
    public String addVoucher(Model model) {



        return "/admin-add-voucher";
    }

    @PostMapping("/addNewVoucher")
    public String addNewVoucher(@ModelAttribute("voucherDt") Voucherdt voucherdt,
            RedirectAttributes redirectAttributes) {



        return "redirect:/admin-customers";
    }

    @GetMapping("/admin-customers")
    public String viewCustomers(Model model) {

        List<User> users = userService.getAllUsers();

        model.addAttribute("users", users);
        return "/admin-customers";
    }

}