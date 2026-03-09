package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition.predicate;

import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

public interface ConditionPredicate {
    boolean checkCondition(Condition condition, SensorStateAvro sensorStateAvro);

    ConditionTypeAvro getConditionType();
}
