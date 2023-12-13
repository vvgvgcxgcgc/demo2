package com.example.demo.Service;

import com.example.demo.Domain.User;
import com.example.demo.Respories.UserRepo;
import com.example.demo.dto.Userdt;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service

public class Userserimp implements Userser{
    private final UserRepo userRepo;
    @Autowired
     public Userserimp(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User save(Userdt userdt) {
        User user = User.builder()
                .Fullname(userdt.getFullname())
                .username(userdt.getUsername())
                .Password(userdt.getPassword())
                .Phonenumber(userdt.getPhonenumber())
                .Checkuser(1)
                .addresses(new ArrayList<>())
                .build();
        user.getAddresses().add(userdt.getAddress());
       return userRepo.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}
