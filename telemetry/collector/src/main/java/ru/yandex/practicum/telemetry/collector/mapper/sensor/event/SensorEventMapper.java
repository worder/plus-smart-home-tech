package ru.yandex.practicum.telemetry.collector.mapper.sensor.event;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.SensorEventType;

public interface SensorEventMapper {
    SensorEventAvro mapToSensorEventAvro(SensorEvent sensorEvent);

    SensorEventType getEventType();
}
