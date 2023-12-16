package com.example.demo.dto;

import com.example.demo.Domain.Feedback;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Productdt {
    private String id;

    private String Name;
    private Long Price;
    private String Info;
    private String Image;
    private  List<Feedback> fb;
}
