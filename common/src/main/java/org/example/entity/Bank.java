package org.example.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Bank extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private String bankName;

    private String bankCode;

    @OneToMany(mappedBy = "bank")
    private List<User> user = new ArrayList<>();
}
