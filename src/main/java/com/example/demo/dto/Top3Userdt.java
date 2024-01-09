package com.example.demo.dto;

import com.example.demo.Domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Top3Userdt {
    User user;
    Long amount;
}
