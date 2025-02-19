import os
import time
from kafka import KafkaProducer
from watchdog.observers import Observer
from watchdog.events import FileSystemEventHandler

KAFKA_TOPIC = "DEAN_JSON_DATA"
KAFKA_BROKER = "Dean-kafka:9092"
DATA_FOLDER = "/app/data"

class NewFileHandler(FileSystemEventHandler):
    def __init__(self, producer, topic):
        self.producer = producer
        self.topic = topic

    def on_created(self, event):
        if event.is_directory:
            return
        try:
            with open(event.src_path, 'r') as file:
                data = file.read().strip()
                self.producer.send(self.topic, value=data.encode('utf-8'))
                print(f"Sent data to Kafka: {data}")
        except Exception as e:
            print(f"Error processing file {event.src_path}: {e}")

def main():
    producer = KafkaProducer(bootstrap_servers=KAFKA_BROKER)
    event_handler = NewFileHandler(producer, KAFKA_TOPIC)
    observer = Observer()
    observer.schedule(event_handler, path=DATA_FOLDER, recursive=False)
    observer.start()

    print(f"Watching for new files in {DATA_FOLDER}...")
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        observer.stop()
    observer.join()

if __name__ == "__main__":
    main()
