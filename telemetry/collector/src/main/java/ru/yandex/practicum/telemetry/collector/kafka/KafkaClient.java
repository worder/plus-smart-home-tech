package ru.yandex.practicum.telemetry.collector.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.serializer.GeneralAvroSerializer;

import java.util.Properties;

@Component
public class KafkaClient {
    private Producer<String, SpecificRecordBase> producer;

    @Value("${telemetry.collector.kafka.producer.bootstrap.servers}")
    private String bootstrapServers;

    public Producer<String, SpecificRecordBase> getProducer() {
        if (producer == null) {
            Properties config = new Properties();
            config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                    "org.apache.kafka.common.serialization.StringSerializer");
            config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);

            producer = new KafkaProducer<>(config);
        }

        return producer;
    }

    public void send(String topic, SpecificRecordBase record) {
        this.getProducer().send(new ProducerRecord<>(topic, record));
    }
}
