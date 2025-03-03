package com.tveritin.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tveritin.config.RequestQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostRequestDumpSchedulerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RequestQueue requestQueue;

    @InjectMocks
    private PostRequestDumpScheduler scheduler;

    private String testDirectory = "target/test-output";

    @BeforeEach
    void setUp() throws IOException {
        scheduler = new PostRequestDumpScheduler(objectMapper, requestQueue);
        scheduler.directoryPath = testDirectory;
        setFileCounter(1);
        // Очищаем тестовую директорию перед каждым тестом
        Path dir = Path.of(testDirectory);
        if (Files.exists(dir)) {
            Files.walk(dir)
                    .sorted((a, b) -> b.compareTo(a)) // Удаляем начиная с вложенных файлов
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        Files.createDirectories(dir); // Создаем директорию заново
    }

    @Test
    void testFlushRequestQueueToFile_EmptyQueue() {
        when(requestQueue.isEmpty()).thenReturn(true);
        scheduler.flushRequestQueueToFile();
        verify(requestQueue, times(1)).isEmpty();
        verifyNoMoreInteractions(requestQueue, objectMapper);
    }

    @Test
    void testFlushRequestQueueToFile_SuccessfulWrite() throws JsonProcessingException, IOException {
        LinkedBlockingQueue<Object> mockQueue = new LinkedBlockingQueue<>();
        mockQueue.add("testRequest1");
        mockQueue.add("testRequest2");

        when(requestQueue.isEmpty()).thenAnswer(invocation -> mockQueue.isEmpty());
        when(requestQueue.poll()).thenAnswer(invocation -> mockQueue.poll());
        when(objectMapper.writeValueAsString(anyList())).thenReturn("[\"testRequest1\", \"testRequest2\"]");

        scheduler.flushRequestQueueToFile();

        verify(requestQueue, atLeastOnce()).isEmpty();
        verify(requestQueue, times(2)).poll();
        verify(objectMapper).writeValueAsString(anyList());

        String fileName = findGeneratedFileName(testDirectory);
        File file = new File(fileName);
        assertTrue(file.exists(), "Файл должен быть создан");
    }

    @Test
    @Disabled("too long")
    void testFlushRequestQueueToFile_Timeout() throws JsonProcessingException {
        LinkedBlockingQueue<Object> mockQueue = new LinkedBlockingQueue<>();
        mockQueue.add("testRequest1");
        mockQueue.add("testRequest2");

        when(requestQueue.isEmpty()).thenAnswer(invocation -> mockQueue.isEmpty());
        when(requestQueue.poll()).thenAnswer(invocation -> {
            Thread.sleep(60_000);
            return mockQueue.poll();
        });
        when(objectMapper.writeValueAsString(anyList())).thenReturn("[\"testRequest1\"]");

        scheduler.flushRequestQueueToFile();

        verify(requestQueue, atLeastOnce()).isEmpty();
        verify(requestQueue, times(1)).poll();
        verify(objectMapper).writeValueAsString(anyList());
    }

    @Test
    void testFlushRequestQueueToFile_JsonProcessingException() throws JsonProcessingException {
        LinkedBlockingQueue<Object> mockQueue = new LinkedBlockingQueue<>();
        mockQueue.add("testRequest");

        when(requestQueue.isEmpty()).thenAnswer(invocation -> mockQueue.isEmpty());
        when(requestQueue.poll()).thenAnswer(invocation -> mockQueue.poll());
        when(objectMapper.writeValueAsString(anyList())).thenThrow(new JsonProcessingException("JSON error") {});

        scheduler.flushRequestQueueToFile();

        verify(requestQueue, atLeastOnce()).isEmpty();
        verify(requestQueue, times(1)).poll();
        verify(objectMapper).writeValueAsString(anyList());

        String fileName = scheduler.generateFileName(); // Используем точное имя файла
        File file = new File(fileName);
        assertFalse(file.exists(), "Файл не должен быть создан при исключении");
    }

    @Test
    void testGenerateFileName() {
        String fileName = scheduler.generateFileName();
        assertTrue(fileName.startsWith(testDirectory + "/requests_"), "Имя файла должно начинаться с правильного префикса");
        assertTrue(fileName.endsWith(".json"), "Имя файла должно заканчиваться на .json");
        assertTrue(fileName.matches(".*_\\d+\\.json$"), "Имя файла должно содержать счетчик (цифру) перед .json");
    }

    private void setFileCounter(int value) {
        try {
            java.lang.reflect.Field field = PostRequestDumpScheduler.class.getDeclaredField("fileCounter");
            field.setAccessible(true);
            field.set(null, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set fileCounter", e);
        }
    }

    private String findGeneratedFileName(String directory) {
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.startsWith("requests_") && name.endsWith(".json"));
        if (files != null && files.length > 0) {
            return files[files.length - 1].getPath();
        }
        return directory + "/requests_unknown.json";
    }
}