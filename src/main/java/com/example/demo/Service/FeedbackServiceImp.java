package com.example.demo.Service;

import com.example.demo.Domain.Feedback;
import com.example.demo.Domain.Product;
import com.example.demo.Domain.User;
import com.example.demo.Respositories.FeedbackRepo;
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
     private final UserService userService;
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
            int times = userService.productTimesOrder(user.getId(),product.getId());
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

    @Override
    public Feedback find(Long id) {
        return feedbackRepo.getReferenceById(id);
    }

    @Override
    public Feedback updateOrder(Long id) {
        Feedback feedback = feedbackRepo.getReferenceById(id);
        feedback.setStatus(1);
        return feedbackRepo.save(feedback);
    }

    @Override
    public void  deleteFB(Long id) {
         feedbackRepo.deleteById(id);
    }
}
