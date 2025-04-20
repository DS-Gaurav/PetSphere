package com.examplejjwt.jwtauth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
    @Column(name = "google_login")
    private boolean glogin=false;
    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Blog> blogs = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Pet> pets = new ArrayList<>();
//    public Set<String> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<String> roles) {
//        this.roles = roles;
//    }
}
