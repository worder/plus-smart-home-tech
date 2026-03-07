package ru.yandex.practicum.telemetry.aggregator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.aggregator.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.aggregator.service.AggregatorService;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private final AggregatorService aggregatorService;
    private final KafkaClient kafkaClient;

    @Value("${telemetry.aggregator.kafka.consumer.topic.sensors}")
    private String topicSensorsTelemetry;

    public void start() {
        Consumer<String, SpecificRecordBase> consumer = kafkaClient.getConsumer();

        try {
            consumer.subscribe(List.of(topicSensorsTelemetry));

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(Duration.ofSeconds(5));
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    aggregatorService.aggregateSensorEvent((SensorEventAvro) record.value());
                }

                consumer.commitAsync();
            }
        } catch (WakeupException ignore) {
        } finally {
            try {
                consumer.commitSync();
            } finally {
                log.info("Closing consumer");
                consumer.close();
            }
        }
    }
}
