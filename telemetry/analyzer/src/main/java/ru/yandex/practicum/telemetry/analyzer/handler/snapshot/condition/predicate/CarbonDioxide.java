package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition.predicate;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class CarbonDioxide extends BaseConditionPredicate {
    @Override
    public boolean checkCondition(Condition condition, SensorStateAvro sensorStateAvro) {
        ClimateSensorAvro sensor = (ClimateSensorAvro) sensorStateAvro.getData();
        return checkOperation(condition, sensor.getCo2Level());
    }

    @Override
    public ConditionTypeAvro getConditionType() {
        return  ConditionTypeAvro.CO2LEVEL;
    }
}
