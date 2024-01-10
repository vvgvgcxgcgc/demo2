package com.example.demo.Service;

import com.example.demo.Domain.User;
import com.example.demo.dto.Userdt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface Userser {
    User save(Userdt userdt);
    public User findByUsername(String username);
    public User Update(MultipartFile imageUser, Userdt userdt);
    public int productTimesOrder(Long userid, String productid);
    public List<User> findnewusers(LocalDateTime startDate, LocalDateTime endDate);
    public User updateAddress(String username, String address);

    public String generateSampleAddress(String username);
}
