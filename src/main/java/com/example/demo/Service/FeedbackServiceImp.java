package com.example.demo.Service;

import com.example.demo.Domain.Feedback;
import com.example.demo.Domain.Product;
import com.example.demo.Domain.User;
import com.example.demo.Respories.FeedbackRepo;
import com.example.demo.Respories.UserRepo;
import com.example.demo.dto.Feedbackdt;
import com.example.demo.dto.Userdt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service

public class FeedbackServiceImp implements FeedbackService{
     private  final FeedbackRepo feedbackRepo;
     private final Userser userser;
    @Override
    public Feedback save(Feedback feedback) {
        return feedbackRepo.save(feedback);

    }

    @Override
    public List<Feedbackdt> getALLFb() {
        List<Feedback> feedbacks= feedbackRepo.findAll();
        List<Feedbackdt> feedbackdts = new ArrayList<>();
        for(Feedback feedback: feedbacks){
            User user = feedback.getUser();
            Product product = feedback.getProduct();
            int times = userser.productTimesOrder(user.getId(),product.getId());
            Userdt userdt = Userdt.builder()
                    .id(user.getId())
                    .Fullname(user.getFullname())
                    .Phonenumber(user.getPhonenumber())
                    .productTimeOrder(times)
                    .build();
            Feedbackdt feedbackdt = Feedbackdt.builder()
                    .id(feedback.getId())
                    .message(feedback.getMessage())
                    .status(feedback.getStatus())
                    .product(feedback.getProduct())
                    .user(userdt)
                    .time(feedback.getTime())
                    .build();
            feedbackdts.add(feedbackdt);
        }
        return feedbackdts;
    }
}
