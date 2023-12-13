package com.example.demo.Service;

import com.example.demo.Domain.User;
import com.example.demo.dto.Userdt;
import org.springframework.stereotype.Service;

@Service
public interface Userser {
    User save(Userdt userdt);
    public User findByUsername(String username);

}
