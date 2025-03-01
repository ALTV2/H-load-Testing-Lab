package com.tveritin.config;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class RequestQueue {

    private final ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<>();

    public void add(Object object) {
        queue.add(object);
    }

    public Object poll() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
