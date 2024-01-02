package com.example.demo.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name ="Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderId")
    private Long id;
    private LocalDateTime time;
    private int paybycash =1;
    private int orderstatus = 0;
    private  String unregister_name;
    private String  unregister_phonenumber;
    private String unregister_address;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "UserId", referencedColumnName = "UserId")
    private User  usr ;

    @OneToMany(cascade = CascadeType.DETACH,mappedBy = "od")
    private List<Product_Order> order_productList;
}