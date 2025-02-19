package com.tveritin.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tveritin.entity.Student;
import com.tveritin.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionSchedulerService {

    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;

    // Путь для хранения файлов
    @Value("${app.scheduler.output.path}")
    private String directoryPath;

    private static int fileCounter = 1;  // Инкрементатор для файлов


    @Scheduled(cron = "0 * * * * *") // Каждую минуту
    public void collectTransactionsAndWriteToFile() {
        List<Student> users = studentRepository.findAll(); //Нужно только новые транзакции, но пока пойдет

        if (users.isEmpty()) {
            System.out.println("Нет транзакций для записи.");
            return;
        }

        try {
            // Преобразуем список транзакций в JSON
            String json = objectMapper.writeValueAsString(users);

            // Генерация нового имени файла с инкрементированным номером
            String fileName = generateFileName();

            // Записываем в новый файл
            writeToFile(json, fileName);

            System.out.println("Транзакции успешно записаны в файл: " + fileName);
        } catch (JsonProcessingException e) {
            System.err.println("Ошибка при преобразовании транзакций в JSON: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    // Генерация имени файла с инкрементированным номером
    private String generateFileName() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = directoryPath + "/deanInfo" + timestamp + "_" + fileCounter++ + ".json";
        return fileName;
    }

    // Запись содержимого JSON в файл
    private void writeToFile(String jsonContent, String fileName) throws IOException {
        File file = new File(fileName);

        // Если файл не существует, создаем его
        if (!file.exists()) {
            file.createNewFile();
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonContent);
//            writer.write("\n");  // Для разделения записей
        }
    }
}
