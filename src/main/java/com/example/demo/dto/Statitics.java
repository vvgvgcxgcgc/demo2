package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Statitics {
    long RevenuebyDay;
    long RevenubyWeek;
    long RevenuebyMonth;

}
