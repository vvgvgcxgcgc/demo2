package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
