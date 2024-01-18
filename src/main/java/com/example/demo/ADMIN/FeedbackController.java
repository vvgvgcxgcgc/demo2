package com.example.demo.ADMIN;

import com.example.demo.Domain.User;
import com.example.demo.Domain.Voucher;
import com.example.demo.Service.UserService;
import com.example.demo.Service.VoucherService;
import com.example.demo.dto.Userdt;
import com.example.demo.Domain.Feedback;
import com.example.demo.Service.FeedbackService;
import com.example.demo.dto.Feedbackdt;

import com.example.demo.dto.Voucherdt;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final UserService userService;
    private final VoucherService voucherService;

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
        model.addAttribute("voucherDt" ,new Voucherdt());



        return "/admin-add-voucher";
    }

    @PostMapping("/addNewVoucher")
    public String addNewVoucher(@ModelAttribute("voucherDt") Voucherdt voucherdt,
            RedirectAttributes redirectAttributes, Model model) {
        if(voucherdt.getValue()>50000){
            model.addAttribute("Error", "Value of voucher exceeds 50.000 VND. Change it!");
            return "/admin-add-voucher";
        }
        else{
            if(voucherdt.getExpired_date().isBefore(LocalDate.now())){
                model.addAttribute("Error", "The datetime is in the past. Change it!");
                return "/admin-add-voucher";
            }
            else{
                List<Integer> range = new ArrayList<>();
                range.add(-1);
                range.add(50);
                range.add(200);
                range.add(1000);
                range.add(Integer.MAX_VALUE-2);
                List<User> users = userService.getUserInRange(range.get(voucherdt.getCheck_range()-1)+1,range.get(voucherdt.getCheck_range()), voucherdt.getSubtractPoint());
                if(users == null){
                    model.addAttribute("Error", "No one can be added this voucher. Change it!");
                    return "/admin-add-voucher";
                }
                else {
                    User user1 = users.get(0);
                    user1 = userService.updateUserPoint(user1, -voucherdt.getSubtractPoint());
                    Voucher voucher = voucherService.save(voucherdt,user1);
                    for(int i= 1;i< users.size();i++){
                        User user = users.get(i);
                        user = userService.updateUserPoint(user, -voucherdt.getSubtractPoint());
                        voucherService.update(voucher,user);
                        redirectAttributes.addFlashAttribute("success", "Added voucher successfully");
                    }

                }

            }

        }

        return "redirect:/admin-customers";
    }

    @GetMapping("/admin-customers")
    public String viewCustomers(Model model) {

        List<User> users = userService.getAllUsers();

        model.addAttribute("users", users);
        return "/admin-customers";
    }

}