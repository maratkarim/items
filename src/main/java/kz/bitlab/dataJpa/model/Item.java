package kz.bitlab.dataJpa.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_item")
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String nameItem;

    @Column(name = "price")
    private int price;

    @ManyToOne(fetch = FetchType.EAGER)
    private Country country;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Category> categories;

}
