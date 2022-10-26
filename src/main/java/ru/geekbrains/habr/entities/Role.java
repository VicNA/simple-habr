package ru.geekbrains.habr.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    private String name;

//    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
//    private List<User> users;


}
