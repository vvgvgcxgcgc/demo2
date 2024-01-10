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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
                .time(LocalDateTime.now())
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
        user.setAddresses(userdt.getAddresses());

        return userRepo.save(user);
    }

    @Override
    public int productTimesOrder(Long userid, String productid) {
        return userRepo.productOrderTimes(userid,productid);
    }

    @Override
    public List<User> findnewusers(LocalDateTime startDate, LocalDateTime endDate) {
        return userRepo.findNewUsers(startDate,endDate);
    }

    @Override
    public User updateAddress(String username, String address) {
        User user = userRepo.findByUsername(username);
        for(String ad: user.getAddresses()){
            if(ad.equals(address)) return  user;
        }
        user.getAddresses().add(address);
        return userRepo.save(user);
    }

    @Override
    public String generateSampleAddress(String username) {
        User user = userRepo.findByUsername(username);

        String address = user.getAddresses().getFirst();
        String[] parts = address.split(",");
        int index;
        if(parts.length>=5) index = parts.length-2;
        else if(parts.length>=2) index = parts.length-1;
        else  index =1;
        String sample = "";
        int j = parts.length-1;
        while (index>0){
            sample = parts[j]+sample;
            j--; index--;
        }
        return sample;


    }
}
