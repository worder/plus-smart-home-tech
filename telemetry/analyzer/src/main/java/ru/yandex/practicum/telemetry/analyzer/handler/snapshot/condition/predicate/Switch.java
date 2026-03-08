package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition.predicate;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class Switch extends BaseConditionPredicate {
    @Override
    public boolean checkCondition(Condition condition, SensorStateAvro sensorStateAvro) {
        SwitchSensorAvro sensor = (SwitchSensorAvro) sensorStateAvro.getData();
        return checkOperation(condition, sensor.getState() ? 1 : 0);
    }

    @Override
    public ConditionTypeAvro getConditionType() {
        return ConditionTypeAvro.SWITCH;
    }
}
