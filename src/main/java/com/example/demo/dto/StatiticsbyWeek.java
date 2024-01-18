package com.example.demo.dto;

import com.example.demo.Domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatiticsbyWeek {
    List<Integer> orderThroughHour;
    Long revenuebyWeek;
    Long orderSuccessAmount;
    double cancelOrderRate;
    List<Top3Productdt> top3Productdts;


}
