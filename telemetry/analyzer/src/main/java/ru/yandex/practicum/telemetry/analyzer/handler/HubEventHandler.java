package ru.yandex.practicum.telemetry.analyzer.handler;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler<T> {
    void handleEvent(HubEventAvro event);

    Class<T> getType();
}
