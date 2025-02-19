FROM bitnami/spark:latest

WORKDIR /app

COPY target/spark-streaming-service-1.0-SNAPSHOT.jar app.jar

CMD ["spark-submit", "--class", "com.tveritin.SparkStreamingApp", "--master", "local[*]", "app.jar"]
