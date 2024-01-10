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
public class StatiticsbyMonth {
    Long revenuebyMonth;
    Long orderSuccessAmount;
    double cancelOrderRate;
    List<Top3Userdt> top3Userdts;
    List<User> newusers;


}
