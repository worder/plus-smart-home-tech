package ru.yandex.practicum.telemetry.collector.mapper.hub.event.scenario;

import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.scenario.ScenarioCondition;

public class ScenarioConditionMapper {
    public static ScenarioConditionAvro mapToAvro(ScenarioCondition data) {
        return ScenarioConditionAvro.newBuilder()
                .setType(ConditionTypeAvro.valueOf(data.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(data.getOperation().name()))
                .setSensorId(data.getSensorId())
                .setValue(data.getValue())
                .build();
    }
}
