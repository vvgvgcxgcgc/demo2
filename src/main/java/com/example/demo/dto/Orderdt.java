package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orderdt {
    public static int countOrder =0;
    public static int check =0;
    private String id_user;
    private  String name;
    private String  phonenumber;
    private String address;
    private Integer totalPrice;

}
