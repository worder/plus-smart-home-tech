package ru.yandex.practicum.telemetry.collector.controller;

import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.SensorEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.service.EventProcessingService;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventProcessingService eventProcessingService;

    @PostMapping("/sensors")
    public void sensorEvent(@Valid @RequestBody SensorEvent event) {
        log.info("Sensor event: {}", event);
        eventProcessingService.processSensorEvent(event);
    }

    @PostMapping("/hubs")
    public void hubEvent(@Valid @RequestBody HubEvent event) {
        log.info("Hub event: {}", event);
        eventProcessingService.processHubEvent(event);
    }
}
