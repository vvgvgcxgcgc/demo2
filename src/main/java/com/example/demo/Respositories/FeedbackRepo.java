package com.example.demo.Respositories;

import com.example.demo.Domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface FeedbackRepo extends JpaRepository<Feedback, Long> {
}
