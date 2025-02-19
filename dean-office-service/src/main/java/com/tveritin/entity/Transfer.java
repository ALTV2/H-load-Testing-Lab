package com.tveritin.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_account_id", referencedColumnName = "id")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id", referencedColumnName = "id")
    private Account toAccount;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double fee;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String transactionId;

    @Column(nullable = false)
    private LocalDateTime transferDate;
}
