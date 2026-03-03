package ru.yandex.practicum.telemetry.collector.mapper.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.mapper.sensor.event.SensorEventMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SensorEventsMapper {
    public final Map<SensorEventType, SensorEventMapper> sensorEventMappers = new HashMap<>();

    SensorEventsMapper(List<SensorEventMapper> mapperBeans) {
        for (SensorEventMapper sensorEventMapper : mapperBeans) {
            sensorEventMappers.put(sensorEventMapper.getEventType(), sensorEventMapper);
        }
    }

    public SensorEventAvro mapToSensorEventAvro(SensorEvent sensorEvent) {
        if (sensorEventMappers.containsKey(sensorEvent.getType())) {
            return sensorEventMappers.get(sensorEvent.getType()).mapToSensorEventAvro(sensorEvent);
        }

        throw new RuntimeException("Unknown sensor event type:  " + sensorEvent.getType());
    }
}
