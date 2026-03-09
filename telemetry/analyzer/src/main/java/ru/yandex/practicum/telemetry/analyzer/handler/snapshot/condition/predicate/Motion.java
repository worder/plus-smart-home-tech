package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition.predicate;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class Motion extends BaseConditionPredicate {
    @Override
    public boolean checkCondition(Condition condition, SensorStateAvro sensorStateAvro) {
        MotionSensorAvro sensor = (MotionSensorAvro) sensorStateAvro.getData();
        return this.checkOperation(condition, sensor.getMotion() ? 1 : 0);
    }

    @Override
    public ConditionTypeAvro getConditionType() {
        return ConditionTypeAvro.MOTION;
    }
}
