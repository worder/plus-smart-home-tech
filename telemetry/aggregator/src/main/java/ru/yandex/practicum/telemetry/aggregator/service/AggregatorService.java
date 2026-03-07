package ru.yandex.practicum.telemetry.aggregator.service;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface AggregatorService {
    void aggregateSensorEvent(SensorEventAvro sensorEvent);
}
