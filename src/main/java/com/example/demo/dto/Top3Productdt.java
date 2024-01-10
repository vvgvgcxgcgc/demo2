package com.example.demo.dto;

import com.example.demo.Domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Top3Productdt {
    private Product product;
    private Long amount;

}
