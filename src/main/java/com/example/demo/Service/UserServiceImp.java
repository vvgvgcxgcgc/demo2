package com.example.demo.Service;

import com.example.demo.Domain.Product;
import com.example.demo.Domain.User;
import com.example.demo.Respositories.UserRepo;
import com.example.demo.Utils.ImageUpload;
import com.example.demo.dto.Userdt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service

public class UserServiceImp implements UserService {
    private final UserRepo userRepo;
    private final ImageUpload imageUpload;
    @Autowired
    public UserServiceImp(UserRepo userRepo, ImageUpload imageUpload) {
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

        String address = user.getAddresses().get(0);
        String[] parts = address.split(",");
        int index;
        if(parts.length>=5) index = parts.length;
        else if(parts.length>=2) index = parts.length-1;
        else index =1;
        String sample = "";
        int j = parts.length-1;
        while (index>0){
            sample = parts[j] + sample;
            j--; index--;
        }
        return sample;
    }

    @Override
    public User updateFirstAddress(String username, String address) {
        User user = userRepo.findByUsername(username);
        for (int i=0; i<user.getAddresses().size(); i++){
            if(user.getAddresses().get(i).equals(address)){
                user.getAddresses().remove(i);
                break;
            }
        }
        user.getAddresses().add(0,address);
        return userRepo.save(user);
    }

    @Override
    public User updatePassword(String username, String password) {
        User user = userRepo.findByUsername(username);
        user.setPassword(password);
        return userRepo.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepo.findAll();
    }

    @Override
    public List<User> getUserInRange(int down, int top, int min_point) {
        List<User> users = userRepo.findUserInRange(down,top,min_point);

        if(users.size()>0){

            return users;
        }
        return null;
    }

    @Override
    public User updateVoucher(User user) {
        user.getVoucherList().removeIf(element -> element.getExpireDate().isBefore(LocalDate.now()));
        return userRepo.save(user);
    }

    @Override
    public User deleteVoucherUser(User user, Long VC_id) {
        user.getVoucherList().removeIf(element -> element.getId()== VC_id);
        return userRepo.save(user);

    }

    @Override
    public User updateUserPoint(User user, int point) {

        user.setUserpoint(user.getUserpoint()+point);
        return userRepo.save(user);
    }
}
