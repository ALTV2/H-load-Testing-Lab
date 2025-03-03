package com.tveritin.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tveritin.config.RequestQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostRequestDumpScheduler {

    private final ObjectMapper objectMapper;
    private final RequestQueue requestQueue;

    @Value("${app.scheduler.output.path}")
    protected String directoryPath;

    private static int fileCounter = 1;

    @Scheduled(cron = "0 * * * * *") // Каждую минуту
    public void flushRequestQueueToFile() {
        List<Object> requestsToWrite = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        long maxDuration = 55_000; // 55 секунд в миллисекундах

        while (!requestQueue.isEmpty()) {
            if (System.currentTimeMillis() - startTime >= maxDuration) {
                System.out.println("Достигнут лимит времени 55 секунд, прерываем цикл");
                break;
            }
            requestsToWrite.add(requestQueue.poll());
        }

        if (requestsToWrite.isEmpty()) {
            System.out.println("Нет новых запросов для записи.");
            return;
        }

        try {
            String json = objectMapper.writeValueAsString(requestsToWrite);
            String fileName = generateFileName();
            writeToFile(json, fileName);
            System.out.println("Запросы успешно записаны в файл: " + fileName);
        } catch (JsonProcessingException e) {
            System.err.println("Ошибка при преобразовании в JSON: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    protected String generateFileName() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return directoryPath + "/requests_" + timestamp + "_" + fileCounter++ + ".json";
    }

    private void writeToFile(String jsonContent, String fileName) throws IOException {
        File file = new File(fileName);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs(); // Создаем родительские директории, если их нет
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonContent);
        }
    }
}