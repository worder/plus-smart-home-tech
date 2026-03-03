package ru.yandex.practicum.telemetry.collector.mapper.sensor.event;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.SensorEvent;

public abstract class BaseSensorEventMapper <T extends SpecificRecordBase> implements SensorEventMapper {
    @Override
    public SensorEventAvro mapToSensorEventAvro(SensorEvent sensorEvent) {
        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(this.mapToPayloadAvro(sensorEvent))
                .build();
    }

    public abstract T mapToPayloadAvro(SensorEvent sensorEvent);
}
