package com.examplejjwt.jwtauth.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String mobile;
    @Column(nullable = false,unique = true)
    private String email;
    private int age;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Profile(String umobile, String uemail, int uage, User user) {
        this.mobile=umobile;
        this.email=uemail;
        this.age=uage;
        this.user= user;
    }
}
