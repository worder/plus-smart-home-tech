package ru.yandex.practicum.telemetry.collector.mapper.hub.event;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEventType;

@Component
public class DeviceRemovedEventMapper extends BaseHubEventMapper<DeviceRemovedEventAvro> {
    @Override
    public DeviceRemovedEventAvro mapToPayloadAvro(HubEvent eventDto) {
        DeviceRemovedEvent dto = (DeviceRemovedEvent) eventDto;

        return DeviceRemovedEventAvro.newBuilder()
                .setId(dto.getId())
                .build();
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
