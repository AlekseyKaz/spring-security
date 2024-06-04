package com.dhabits.ss.demo.domain.entity;

import jakarta.persistence.*;
import jdk.jfr.Unsigned;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(unique = true)
    private String login;
    private String password;

}
