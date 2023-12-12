package Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name ="Order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "OrderId")
    private Long id;
    private LocalDateTime time;

    private Integer Paybycash;
    private Integer Orderstatus;
    @ManyToOne
    @JoinColumn(name = "UserId")
    private User  user ;

    @OneToMany(mappedBy = "order")
    private List<Product_Order> order_productList;



}
