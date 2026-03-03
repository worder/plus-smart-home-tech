package ru.yandex.practicum.telemetry.collector.mapper.hub.event;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEventType;

@Component
public class DeviceAddedEventMapper extends BaseHubEventMapper<DeviceAddedEventAvro> {
    @Override
    protected DeviceAddedEventAvro mapToPayloadAvro(HubEvent eventDto) {
        DeviceAddedEvent dto = (DeviceAddedEvent) eventDto;

        return DeviceAddedEventAvro.newBuilder()
                .setId(dto.getId())
                .setType(DeviceTypeAvro.valueOf(dto.getDeviceType().name()))
                .build();
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.DEVICE_ADDED;
    }
}
