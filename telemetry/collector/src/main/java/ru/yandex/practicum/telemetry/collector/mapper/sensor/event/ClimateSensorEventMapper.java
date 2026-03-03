package ru.yandex.practicum.telemetry.collector.mapper.sensor.event;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.ClimateSensorEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.sensor.SensorEventType;

@Component
public class ClimateSensorEventMapper extends  BaseSensorEventMapper<ClimateSensorAvro> {
    @Override
    public ClimateSensorAvro mapToPayloadAvro(SensorEvent event) {
        ClimateSensorEvent dto = (ClimateSensorEvent) event;
        return ClimateSensorAvro.newBuilder()
                .setCo2Level(dto.getCo2Level())
                .setHumidity(dto.getHumidity())
                .setTemperatureC(dto.getTemperatureC())
                .build();
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
