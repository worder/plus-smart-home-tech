package ru.yandex.practicum.telemetry.collector.mapper.hub.event;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.collector.mapper.hub.event.scenario.DeviceActionMapper;
import ru.yandex.practicum.telemetry.collector.mapper.hub.event.scenario.ScenarioConditionMapper;

@Component
public class ScenarioAddedEventMapper extends BaseHubEventMapper<ScenarioAddedEventAvro> {
    @Override
    protected ScenarioAddedEventAvro mapToPayloadAvro(HubEvent eventDto) {
        ScenarioAddedEvent dto = (ScenarioAddedEvent) eventDto;
        return ScenarioAddedEventAvro.newBuilder()
                .setName(dto.getName())
                .setActions(
                        dto.getActions().stream()
                                .map(DeviceActionMapper::mapToAvro)
                                .toList())
                .setConditions(
                        dto.getConditions().stream()
                                .map(ScenarioConditionMapper::mapToAvro)
                                .toList())
                .build();
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
