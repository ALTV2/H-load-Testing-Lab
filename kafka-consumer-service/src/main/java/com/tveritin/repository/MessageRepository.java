package com.tveritin.repository;

import com.tveritin.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
}


