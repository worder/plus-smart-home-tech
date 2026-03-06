package ru.yandex.practicum.telemetry.collector.mapper.sensor.event;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.LightSensorEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.SensorEventType;

@Component
public class LightSensorEventMapper extends  BaseSensorEventMapper<LightSensorAvro> {
    @Override
    public LightSensorAvro mapToPayloadAvro(SensorEvent event) {
        LightSensorEvent dto = (LightSensorEvent) event;
        return LightSensorAvro.newBuilder()
                .setLinkQuality(dto.getLinkQuality())
                .setLuminosity(dto.getLuminosity())
                .build();
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
