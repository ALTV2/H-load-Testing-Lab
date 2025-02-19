package com.tveritin.repository;

import com.tveritin.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @NotNull
    List<User> findAll(); // Получаем все записи транзакций
}
