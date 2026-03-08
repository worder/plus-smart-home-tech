package ru.yandex.practicum.telemetry.analyzer.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Action;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioAdded implements HubEventHandler<ScenarioAddedEventAvro> {
    private final ScenarioRepository scenarioRepository;

    @Override
    public void handleEvent(HubEventAvro event) {
        ScenarioAddedEventAvro payload = (ScenarioAddedEventAvro) event.getPayload();
        Scenario scenario = Scenario.builder()
                .hubId(event.getHubId())
                .name(payload.getName())
                .actions(payload.getActions()
                        .stream()
                        .collect(Collectors.toMap(DeviceActionAvro::getSensorId, this::mapToAction)))
                .conditions(payload.getConditions()
                        .stream()
                        .collect(Collectors.toMap(ScenarioConditionAvro::getSensorId, this::mapToCondition)))
                .build();

        scenarioRepository.save(scenario);

        log.info("Scenario added event: {}", payload);
    }

    @Override
    public Class<ScenarioAddedEventAvro> getType() {
        return ScenarioAddedEventAvro.class;
    }

    private Condition mapToCondition(ScenarioConditionAvro scenarioConditionAvro) {
        Object value = scenarioConditionAvro.getValue();
        Integer intValue = switch (value) {
            case null -> null;
            case Integer intVal -> intVal;
            case Boolean boolVal -> boolVal ? 1 : 0;
            default -> throw new RuntimeException("Unknown value type: " + value);
        };
        return Condition.builder()
                .type(scenarioConditionAvro.getType())
                .operation(scenarioConditionAvro.getOperation())
                .value(intValue).build();
    }

    private Action mapToAction(DeviceActionAvro deviceActionAvro) {
        return Action.builder()
                .type(deviceActionAvro.getType())
                .build();
    }
}
