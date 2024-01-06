package com.example.demo.dto;

import com.example.demo.Domain.Feedback;
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
    private String name;
    private Long price;
    private Boolean deleted;
    private String info;
    private  String image;
    private List<Feedback> feedBackList;
}
