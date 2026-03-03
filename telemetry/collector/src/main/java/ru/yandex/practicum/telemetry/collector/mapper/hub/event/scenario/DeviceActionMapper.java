package ru.yandex.practicum.telemetry.collector.mapper.hub.event.scenario;

import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.scenario.DeviceAction;

public class DeviceActionMapper {
    public static DeviceActionAvro mapToAvro(DeviceAction data) {
        return DeviceActionAvro.newBuilder()
                .setType(ActionTypeAvro.valueOf(data.getType().name()))
                .setSensorId(data.getSensorId())
                .setValue(data.getValue())
                .build();
    }
}
