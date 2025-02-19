package com.tveritin.repository;

import com.tveritin.entity.Transfer;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {
    @NotNull
    List<Transfer> findAll(); // Получаем все записи транзакций
}
