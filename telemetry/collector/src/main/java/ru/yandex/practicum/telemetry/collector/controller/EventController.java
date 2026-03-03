package ru.yandex.practicum.telemetry.collector.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.service.EventProcessingService;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventProcessingService eventProcessingService;

    @PostMapping("/sensors")
    public void sensorEvent(@Valid @RequestBody SensorEvent event) {
        eventProcessingService.processSensorEvent(event);
    }

    @PostMapping("/hubs")
    public void hubEvent(@Valid @RequestBody HubEvent event) {
        eventProcessingService.processHubEvent(event);
    }
}
