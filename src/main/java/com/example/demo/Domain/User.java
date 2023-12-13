package com.example.demo.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long id;

    private String Fullname;
    private String Phonenumber;
    private String username;
    private String Password;
    private Integer Checkuser;
    private Integer Userpoint;
    @OneToMany(cascade = CascadeType.DETACH,mappedBy = "usr")
    private List<Order>  orders;
    @ElementCollection
    private List<String> addresses;
    @ManyToMany(mappedBy = "userList")
    private List<Voucher> voucherList;
    @OneToMany(cascade = CascadeType.DETACH,mappedBy = "user")
    private List<Feedback> fb;




}
