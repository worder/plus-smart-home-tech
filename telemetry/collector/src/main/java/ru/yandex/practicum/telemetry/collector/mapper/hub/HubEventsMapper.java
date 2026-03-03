package ru.yandex.practicum.telemetry.collector.mapper.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.mapper.hub.event.HubEventMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HubEventsMapper {
    private final Map<HubEventType, HubEventMapper> hubEventMappers = new HashMap<>();

    HubEventsMapper(List<HubEventMapper> mapperBeans) {
        for  (HubEventMapper hubEventMapper : mapperBeans) {
            hubEventMappers.put(hubEventMapper.getEventType(), hubEventMapper);
        }
    }

    public HubEventAvro mapToHubEventAvro(HubEvent event) {
        if (hubEventMappers.containsKey(event.getType())) {
            return hubEventMappers.get(event.getType()).mapToHubEventAvro(event);
        }

        throw new RuntimeException("Unknown hub event type:  " + event.getType());
    }
}
