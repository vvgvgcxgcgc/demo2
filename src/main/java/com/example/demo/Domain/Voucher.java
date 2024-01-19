package com.example.demo.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDate expireDate;
    private int check_range;
    private Long min_ordervalue;
    private  int subtractPoint;
    private String description;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "User_Voucher",
            joinColumns = { @JoinColumn(name = "VoucherId",referencedColumnName = "VoucherId") },
            inverseJoinColumns = { @JoinColumn(name = "UserId",referencedColumnName = "UserId") }
    )
    private List<User> userList;


}
