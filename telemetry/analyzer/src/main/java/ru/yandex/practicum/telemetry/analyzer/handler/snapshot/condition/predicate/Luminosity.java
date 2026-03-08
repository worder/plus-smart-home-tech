package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition.predicate;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class Luminosity extends BaseConditionPredicate {
    @Override
    public boolean checkCondition(Condition condition, SensorStateAvro sensorStateAvro) {
        LightSensorAvro sensor = (LightSensorAvro) sensorStateAvro.getData();
        return this.checkOperation(condition, sensor.getLuminosity());
    }

    @Override
    public ConditionTypeAvro getConditionType() {
        return ConditionTypeAvro.LUMINOSITY;
    }
}
