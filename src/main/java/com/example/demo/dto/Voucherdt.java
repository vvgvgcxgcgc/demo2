package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucherdt {
    Long value;
    String description;
    int subtractPoint;
    int check_range; // 0-50 :1; 51 -> 200 : 2; 201 -> 1000: 3; 1000 -> inf: 4
    Long min_ordervalue;
    LocalDate expired_date;


}
