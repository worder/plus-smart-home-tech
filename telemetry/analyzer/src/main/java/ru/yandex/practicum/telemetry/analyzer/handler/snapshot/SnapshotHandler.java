package ru.yandex.practicum.telemetry.analyzer.handler.snapshot;

import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.client.HubRouterClient;
import ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition.predicate.ConditionPredicate;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SnapshotHandler {
    private final ScenarioRepository scenarioRepository;
    private final HubRouterClient hubRouterClient;

    private Map<ConditionTypeAvro, ConditionPredicate> conditionPredicates = new HashMap<>();

    public SnapshotHandler(
            ScenarioRepository scenarioRepository,
            List<ConditionPredicate> predicateBeans,
            HubRouterClient hubRouterClient) {
        this.scenarioRepository = scenarioRepository;
        this.conditionPredicates = predicateBeans.stream()
                .collect(Collectors.toMap(ConditionPredicate::getConditionType, p -> p));
        this.hubRouterClient = hubRouterClient;
    }

    @Transactional(readOnly = true)
    public void handleSnapshot(SensorsSnapshotAvro snapshot) {
        scenarioRepository.findAllByHubId(snapshot.getHubId()).stream()
                .filter(scenario -> checkConditions(scenario.getConditions(), snapshot.getSensorsState()))
                .forEach(this::sendActions);
    }

    private boolean checkConditions(Map<String, Condition> conditions, Map<String, SensorStateAvro> sensors) {
        return conditions.entrySet().stream().allMatch(e -> {
            if (!sensors.containsKey(e.getKey())) {
                return false;
            }
            return conditionPredicates.get(e.getValue().getType())
                    .checkCondition(e.getValue(), sensors.get(e.getKey()));
        });
    }

    private void sendActions(Scenario scenario) {
        log.info("Sending actions for scenario: {}", scenario.getName());
        Instant now = Instant.now();
        Timestamp ts = Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();

        scenario.getActions().forEach((key, action) -> {
            hubRouterClient.sendAction(DeviceActionRequest.newBuilder()
                    .setHubId(scenario.getHubId())
                    .setScenarioName(scenario.getName())
                    .setAction(DeviceActionProto.newBuilder()
                            .setSensorId(key)
                            .setType(ActionTypeProto.valueOf(action.getType().name()))
                            .setValue(action.getValue())
                            .build())
                    .setTimestamp(ts)
                    .build());
        });
    }
}
