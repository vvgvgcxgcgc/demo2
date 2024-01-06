package com.example.demo.Respories;

import com.example.demo.Domain.Feedback;
import com.example.demo.Domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface FeedbackRepo extends JpaRepository<Feedback, Long> {
}
