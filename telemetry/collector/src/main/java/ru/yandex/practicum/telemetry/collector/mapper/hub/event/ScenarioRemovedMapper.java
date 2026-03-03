package ru.yandex.practicum.telemetry.collector.mapper.hub.event;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.ScenarioRemovedEvent;

@Component
public class ScenarioRemovedMapper extends BaseHubEventMapper<ScenarioRemovedEventAvro> {
    @Override
    protected ScenarioRemovedEventAvro mapToPayloadAvro(HubEvent eventDto) {
        ScenarioRemovedEvent dto = (ScenarioRemovedEvent) eventDto;
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(dto.getName())
                .build();
    }

    @Override
    public HubEventType getEventType() {
        return  HubEventType.SCENARIO_REMOVED;
    }
}
