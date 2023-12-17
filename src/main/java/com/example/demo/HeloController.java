package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//@RequiredArgsConstructor
@Controller
public class HeloController {
    @GetMapping( "/test")
    public String Hello(){
        return "add_product(ky)";
    }
}
