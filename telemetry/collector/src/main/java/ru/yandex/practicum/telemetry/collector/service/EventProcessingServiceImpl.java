package ru.yandex.practicum.telemetry.collector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.mapper.hub.HubEventsMapper;
import ru.yandex.practicum.telemetry.collector.mapper.sensor.SensorEventsMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventProcessingServiceImpl implements EventProcessingService {
    private final HubEventsMapper hubEventsMapper;
    private final SensorEventsMapper sensorEventsMapper;
    private final KafkaClient kafkaClient;

    @Value("${telemetry.collector.kafka.producer.topic.hub-events}")
    private String hubEventsTopic;
    @Value("${telemetry.collector.kafka.producer.topic.sensor-events}")
    private String sensorEventsTopic;

    @Override
    public void processHubEvent(HubEvent event) {
        log.info("Sending hub event to topic [{}]: {}", hubEventsTopic, event);
        kafkaClient.send(hubEventsTopic, hubEventsMapper.mapToHubEventAvro(event));
    }

    @Override
    public void processSensorEvent(SensorEvent event) {
        log.info("Sending sensor event to topic [{}]: {}", sensorEventsTopic, event);
        kafkaClient.send(sensorEventsTopic, sensorEventsMapper.mapToSensorEventAvro(event));
    }
}
