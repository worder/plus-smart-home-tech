package ru.yandex.practicum.telemetry.collector.mapper.sensor.event;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.MotionSensorEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.SensorEventType;

@Component
public class MotionSensorEventMapper extends BaseSensorEventMapper<MotionSensorAvro> {
    @Override
    public MotionSensorAvro mapToPayloadAvro(SensorEvent sensorEvent) {
        MotionSensorEvent dto = (MotionSensorEvent) sensorEvent;
        return MotionSensorAvro.newBuilder()
                .setMotion(dto.isMotion())
                .setLinkQuality(dto.getLinkQuality())
                .setVoltage(dto.getVoltage())
                .build();
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
