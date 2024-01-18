package com.example.demo.Service;

import com.example.demo.Domain.User;
import com.example.demo.dto.Userdt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface UserService {
    User save(Userdt userdt);
    public User findByUsername(String username);
    public User Update(MultipartFile imageUser, Userdt userdt);
    public int productTimesOrder(Long userid, String productid);
    public List<User> findnewusers(LocalDateTime startDate, LocalDateTime endDate);
    public User updateAddress(String username, String address);
    public String generateSampleAddress(String username);
    public User updateFirstAddress(String username, String address);
    public User updatePassword (String username, String password);

     public List<User> getAllUsers();
     public List<User> getUserInRange(int down, int top, int min_point);

     public User updateVoucher(User user);
     public User deleteVoucherUser(User user, Long VC_id);
     public User updateUserPoint(User user, int point);

}
