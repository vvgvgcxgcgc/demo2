package com.example.demo.Service;

import com.example.demo.Domain.User;
import com.example.demo.Respories.UserRepo;
import com.example.demo.Utils.ImageUpload;
import com.example.demo.dto.Userdt;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

@Service

public class Userserimp implements Userser{
    private final UserRepo userRepo;
    private final ImageUpload imageUpload;
    @Autowired
     public Userserimp(UserRepo userRepo, ImageUpload imageUpload) {
        this.userRepo = userRepo;
        this.imageUpload = imageUpload;
    }

    @Override
    public User save(Userdt userdt) {
        User user = User.builder()
                .Fullname(userdt.getFullname())
                .username(userdt.getUsername())
                .Password(userdt.getPassword())
                .Phonenumber(userdt.getPhonenumber())
                .Checkuser(1)
                .Userpoint(0)
                .addresses(userdt.getAddresses())
                .build();

       return userRepo.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User Update(MultipartFile imageUser, Userdt userdt) {
        User user  = userRepo.findByUsername(userdt.getUsername());
        try {
            if (imageUser.getBytes().length > 0) {
                if (imageUpload.checkExist(imageUser)) {
                    user.setAvatar(user.getAvatar());
                } else {
                    imageUpload.uploadFile(imageUser);
                    user.setAvatar(Base64.getEncoder().encodeToString(imageUser.getBytes()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        user.setFullname(userdt.getFullname());
        user.setPhonenumber(userdt.getPhonenumber());

        return userRepo.save(user);
    }
}
