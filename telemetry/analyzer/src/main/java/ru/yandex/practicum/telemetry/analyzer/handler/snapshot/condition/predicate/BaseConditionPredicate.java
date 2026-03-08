package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition.predicate;

import ru.yandex.practicum.telemetry.analyzer.model.Condition;

public abstract class BaseConditionPredicate implements ConditionPredicate {
    protected  boolean checkOperation(Condition condition, Integer sensorValue) {
        Integer targetValue = condition.getValue();
        return switch (condition.getOperation()) {
            case EQUALS -> sensorValue.equals(targetValue);
            case LOWER_THAN -> sensorValue < targetValue;
            case GREATER_THAN ->  sensorValue > targetValue;
            case null -> throw new RuntimeException("Unknown condition operation");
        };
    }
}
