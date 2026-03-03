package ru.yandex.practicum.telemetry.collector.mapper.hub.event;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.dto.event.hub.HubEvent;

public abstract class BaseHubEventMapper<T extends SpecificRecordBase> implements HubEventMapper {
    @Override
    public HubEventAvro mapToHubEventAvro(HubEvent event) {
        T payload = this.mapToPayloadAvro(event);

        return HubEventAvro.newBuilder()
                .setPayload(payload)
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .build();
    }

    protected abstract T mapToPayloadAvro(HubEvent eventDto);
}
