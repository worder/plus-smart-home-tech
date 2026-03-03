package ru.yandex.practicum.telemetry.collector.service;

import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.SensorEvent;

public interface EventProcessingService {
    void processSensorEvent(SensorEvent event);

    void processHubEvent(HubEvent event);
}
