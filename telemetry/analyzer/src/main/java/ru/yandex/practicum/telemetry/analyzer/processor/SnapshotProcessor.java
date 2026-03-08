package ru.yandex.practicum.telemetry.analyzer.processor;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.handler.snapshot.SnapshotHandler;
import ru.yandex.practicum.telemetry.deserializer.SensorSnapshotDeserializer;

import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class SnapshotProcessor extends BaseRecordProcessor<SensorsSnapshotAvro> {
    private final SnapshotHandler snapshotHandler;

    @Value("${telemetry.analyzer.kafka.bootstrap-servers}")
    private String bootstrapServers =  "localhost:9092";

    @Value("${telemetry.analyzer.kafka.group-id}")
    private String consumerGroupId = "analyzer";

    @Value("${telemetry.analyzer.kafka.topic.snapshots}")
    private String topic = "telemetry.snapshots.v1";

    @Override
    protected void configureConsumer(Properties config) {
        config.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorSnapshotDeserializer.class.getCanonicalName());
    }

    @Override
    protected List<String> getTopics() {
        return List.of(topic);
    }

    @Override
    protected void handleRecord(SensorsSnapshotAvro record) {
        snapshotHandler.handleSnapshot(record);
    }
}
