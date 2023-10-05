package com.example.springboot.jpa.user.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity @Getter @Builder @AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Point> points;


    public enum Role {
        USER, ADMIN
    }

    public void update(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

}
