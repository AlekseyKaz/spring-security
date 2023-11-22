package com.dhabits.ss.demo.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResourceObjectEntity {

    @Id
    private Integer id;
    private String value;
    private String path;


}
