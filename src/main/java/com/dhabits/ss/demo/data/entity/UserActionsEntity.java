package com.dhabits.ss.demo.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_actions")
public class UserActionsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "admin")
    private boolean admin;
    @Column(name = "regular")
    private boolean regular;

    @OneToOne
    private UserEntity user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserActionsEntity that = (UserActionsEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
