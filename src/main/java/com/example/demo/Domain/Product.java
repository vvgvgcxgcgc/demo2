package Domain;

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
    @ManyToMany(mappedBy = "productList")
    private List<Menu> menuList;
    @OneToMany(mappedBy = "product")
    private List<Product_Order> product_orderList;
}
