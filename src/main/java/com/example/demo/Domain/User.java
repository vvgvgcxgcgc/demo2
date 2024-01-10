package com.example.demo.Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name ="User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private long id;

    private String Fullname;
    private String Phonenumber;
    @Column(unique = true) // Đánh dấu trường này là duy nhất
    @NotNull // Đánh dấu trường này không được để trống
    private String username;
    @NotNull // Đánh dấu trường này không được để trống
    private String Password;
    @NotNull // Đánh dấu trường này không được để trống
    private int Checkuser = 1;
    private int Userpoint =0;
    private LocalDateTime time;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String avatar;
    @OneToMany(cascade = CascadeType.DETACH,mappedBy = "usr")
    private List<Order>  orders;
    @ElementCollection
    private List<String> addresses;
    @ManyToMany(mappedBy = "userList")
    private List<Voucher> voucherList;
    @OneToMany(cascade = CascadeType.DETACH,mappedBy = "user")
    private List<Feedback> fb;




}
