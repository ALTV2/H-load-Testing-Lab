package com.tveritin.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String accountNumber;

    private String currency;
    private Double balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
