package ru.geekbrains.habr.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "statuses")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long id;

    @Column(name = "status_name")
    private String name;

}
