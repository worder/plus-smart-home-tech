package ru.yandex.practicum.telemetry.collector.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.mapper.hub.HubEventsMapper;
import ru.yandex.practicum.telemetry.collector.mapper.sensor.SensorEventsMapper;

@Service
@RequiredArgsConstructor
public class EventProcessingServiceImpl implements EventProcessingService {
    private final HubEventsMapper hubEventsMapper;
    private final SensorEventsMapper sensorEventsMapper;
    private final KafkaClient kafkaClient;

    @Override
    public void processHubEvent(HubEvent event) {
        kafkaClient.send("telemetry.hubs.v1", hubEventsMapper.mapToHubEventAvro(event));
    }

    @Override
    public void processSensorEvent(SensorEvent event) {
        kafkaClient.send("telemetry.sensors.v1", sensorEventsMapper.mapToSensorEventAvro(event));
    }
}
