package ru.yandex.practicum.telemetry.aggregator.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;

public interface KafkaClient extends AutoCloseable {
    public Producer<String, SpecificRecordBase> getProducer();

    public Consumer<String, SpecificRecordBase> getConsumer();
}
