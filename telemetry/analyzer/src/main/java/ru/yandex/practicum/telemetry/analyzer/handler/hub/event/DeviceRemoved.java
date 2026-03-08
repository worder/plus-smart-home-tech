package ru.yandex.practicum.telemetry.analyzer.handler.hub.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceRemoved implements HubEventHandler<DeviceRemovedEventAvro> {
    private final SensorRepository sensorRepository;

    @Override
    public void handleEvent(HubEventAvro event) {
        DeviceRemovedEventAvro payload = (DeviceRemovedEventAvro) event.getPayload();
        sensorRepository.deleteById(payload.getId());
        log.info("Device removed event: {}", payload);
    }

    @Override
    public Class<DeviceRemovedEventAvro> getType() {
        return DeviceRemovedEventAvro.class;
    }
}
