package ru.yandex.practicum.telemetry.collector.dto.event.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.scenario.DeviceAction;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.scenario.ScenarioCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {
    @NotBlank
    private String name;

    @NotNull
    private List<ScenarioCondition> conditions;

    @NotNull
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return  HubEventType.SCENARIO_ADDED;
    }
}
