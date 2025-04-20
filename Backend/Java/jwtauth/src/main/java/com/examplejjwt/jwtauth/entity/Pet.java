package com.examplejjwt.jwtauth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "category")
    private String category;
    @Column(name = "image")
    private String image;
    @Column(name = "breed")
    private String breed;
    @Column(name="age")
    private int age;
    @Column(name = "price")
    private Long price;
    @Column(name = "selling",nullable = false)
    private boolean sell = false;
    @Column(name = "ceretificate")
    private String mceretificate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
