package com.tveritin;

import org.apache.kafka.clients.producer.ProducerRecord;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.api.java.JavaDStream;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

public class SparkStreamingApp {
    private static final String KAFKA_TOPIC = "JSON_DATA";
    private static final String KAFKA_SERVER = "Dean-kafka:9092";
    private static final String WATCH_DIRECTORY = "/app/data/";

    public static void main(String[] args) throws Exception {
        SparkConf conf = new SparkConf().setAppName("SparkStreamingApp").setMaster("local[*]");
        JavaStreamingContext streamingContext = new JavaStreamingContext(new JavaSparkContext(conf), Durations.seconds(5));

        JavaDStream<String> fileStream = streamingContext.textFileStream(WATCH_DIRECTORY);

        fileStream.foreachRDD(rdd -> {
            rdd.foreachPartition(partition -> {
                Properties props = new Properties();
                props.put("bootstrap.servers", "kafka:9092");
                props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
                props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

                try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
                    partition.forEachRemaining(line -> {
                        System.out.println("!!!!!!!!!!!!!!!!!");
                        System.out.println(line);
                        System.out.println("!!!!!!!!!!!!!!!!!");
                        producer.send(new ProducerRecord<>(KAFKA_TOPIC, line));
                    });
                }
            });
        });

        streamingContext.start();
        streamingContext.awaitTermination();
    }
}