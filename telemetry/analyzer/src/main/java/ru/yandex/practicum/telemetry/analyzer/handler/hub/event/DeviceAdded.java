package ru.yandex.practicum.telemetry.analyzer.handler.hub.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceAdded implements HubEventHandler<DeviceAddedEventAvro> {
    private final SensorRepository sensorRepository;

    @Override
    public void handleEvent(HubEventAvro event) {
        DeviceAddedEventAvro payload = (DeviceAddedEventAvro) event.getPayload();
        if (sensorRepository.findById(payload.getId()).isEmpty()) {
            sensorRepository.save(Sensor.builder()
                    .id(payload.getId())
                    .hubId(event.getHubId())
                    .build());
            log.info("Device added event: {}", payload);
        } else {
            log.warn("Duplicate device add: {}", payload);
        }
    }

    @Override
    public Class<DeviceAddedEventAvro> getType() {
        return DeviceAddedEventAvro.class;
    }
}
