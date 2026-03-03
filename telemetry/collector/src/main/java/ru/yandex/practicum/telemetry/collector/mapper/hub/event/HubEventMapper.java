package ru.yandex.practicum.telemetry.collector.mapper.hub.event;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEventType;

public interface HubEventMapper {
    HubEventAvro mapToHubEventAvro(HubEvent event);

    HubEventType getEventType();
}
