package com.example.demo.Service;

import com.example.demo.Domain.Feedback;
import com.example.demo.dto.Feedbackdt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface FeedbackService {
    public Feedback save(Feedback feedback);
    public List<Feedbackdt> getALLFb();
}
