package Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name ="User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "UserId")
    private Long id;

    private String Fullname;
    private String Phonenumber;
    private String Username;
    private String Password;
    private Integer Checkuser;
    private Integer Userpoint;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order>  orders;
    @ElementCollection
    private List<String> addresses;
    @ManyToMany(mappedBy = "userList")
    private List<Voucher> voucherList;




}
