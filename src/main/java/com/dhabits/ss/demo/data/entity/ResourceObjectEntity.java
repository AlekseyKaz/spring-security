package com.dhabits.ss.demo.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "resource")
public class ResourceObjectEntity {

    @Id
    private Integer id;
    private String value;
    private String path;


}
