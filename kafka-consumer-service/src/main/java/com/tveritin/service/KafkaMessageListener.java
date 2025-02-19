package com.tveritin.service;

import com.tveritin.entity.Message;
import com.tveritin.repository.MessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageListener {

    private final MessageRepository messageRepository;

    public KafkaMessageListener(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @KafkaListener(topics = "JSON_DATA", groupId = "default")
    public void listen(String messageContent) {
        System.out.println("принял сообщение: " + messageContent);
        Message message = new Message(messageContent);
        messageRepository.save(message);
        System.out.println("Saved message to MongoDB: " + messageContent);
    }
}
