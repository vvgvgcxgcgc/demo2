package com.example.demo.dto;

import com.example.demo.Domain.Feedback;
import com.example.demo.Domain.Order;
import com.example.demo.Domain.Voucher;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Userdt {
    private Long id;

    private String Fullname;
   // @Size(min =10, max = 10, message = "Phone number must contain 10 digits")
    private String Phonenumber;
    private String Username;
    @Size(min =6 ,message = "Password must contain 6 characters ")
    private String Password;
    private String repeatPassword;

    private List<String> addresses;
    private String avatar;
    private int Userpoint;



}
