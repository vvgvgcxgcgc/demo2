package com.example.demo.dto;

import com.example.demo.Domain.Product;
import com.example.demo.Domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Feedbackdt {
    private Long id;

    private Userdt user;

    private Product product;
    private String message;
    private int status ;

    private LocalDateTime time;
}
