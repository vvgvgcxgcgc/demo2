package com.example.demo.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VoucherId")
    private Long id;
    private Long value;
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "User_Voucher",
            joinColumns = { @JoinColumn(name = "VoucherId",referencedColumnName = "VoucherId") },
            inverseJoinColumns = { @JoinColumn(name = "UserId",referencedColumnName = "UserId") }
    )
    private List<User> userList;


}
