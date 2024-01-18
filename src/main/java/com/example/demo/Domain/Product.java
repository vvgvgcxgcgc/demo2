package com.example.demo.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name ="Product")
public class Product {

    @Id
    @Column(name ="ProductId")
    private String id;
    @Column(name ="ProductName")
    private String Name;
    @Column(name ="ProductPrice")
    private Long Price;
    @Column(name ="ProductInfo")
    private String Info;
    private Boolean deleted = false;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String Image;
    @OneToMany(cascade = CascadeType.DETACH,mappedBy = "pro")
    private List<Product_Order> product_orderList;
    @OneToMany(cascade = CascadeType.DETACH,mappedBy = "product")
    private List<Feedback> fb;

}
