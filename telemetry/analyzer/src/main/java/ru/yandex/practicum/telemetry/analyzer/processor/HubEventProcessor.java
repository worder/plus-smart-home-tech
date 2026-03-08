package ru.yandex.practicum.telemetry.analyzer.processor;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.handler.hub.event.HubEventHandler;
import ru.yandex.practicum.telemetry.deserializer.HubEventDeserializer;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class HubEventProcessor extends BaseRecordProcessor<HubEventAvro> implements Runnable {

    @Value("${telemetry.analyzer.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${telemetry.analyzer.kafka.group-id}")
    private String consumerGroupId;

    @Value("${telemetry.analyzer.kafka.topic.hub-events}")
    private String topic;

    private final Map<Class<?>, HubEventHandler<?>> handlers;

    HubEventProcessor(List<HubEventHandler<?>> hubEventHandlers) {
        this.handlers = hubEventHandlers
                .stream()
                .collect(Collectors.toMap(HubEventHandler::getType, h -> h));
    }

    @Override
    protected void configureConsumer(Properties config) {
        config.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, HubEventDeserializer.class.getCanonicalName());
    }

    @Override
    protected List<String> getTopics() {
        return List.of(this.topic);
    }

    @Override
    protected void handleRecord(HubEventAvro record) {
        if (!handlers.containsKey(record.getPayload().getClass())) {
            throw new RuntimeException("No handler for " + record.getClass());
        }

        handlers.get(record.getPayload().getClass()).handleEvent(record);
    }
}
