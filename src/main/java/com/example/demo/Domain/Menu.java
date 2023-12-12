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
@Table(name ="Menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "MenuId")
    private Long id;
    private Integer Menustatus;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Menu_Product ",
            joinColumns = { @JoinColumn(name = "MenuId") },
            inverseJoinColumns = { @JoinColumn(name = "ProductId") }
    )
    private List<Product> productList;

}
