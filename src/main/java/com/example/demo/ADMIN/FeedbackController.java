package com.example.demo.ADMIN;

import com.example.demo.Domain.Feedback;
import com.example.demo.Service.FeedbackService;
import com.example.demo.dto.Feedbackdt;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log
public class FeedbackController {
    private final FeedbackService feedbackService;

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
}