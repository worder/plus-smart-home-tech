package ru.yandex.practicum.telemetry.analyzer.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
public abstract class BaseRecordProcessor<T extends SpecificRecordBase> implements Runnable{
    protected KafkaConsumer<String, T> consumer;

    protected KafkaConsumer<String, T> getConsumer() {
        if (consumer == null) {
            Properties config = new Properties();

            this.configureConsumer(config);
            this.consumer = new KafkaConsumer<>(config);

            Runtime.getRuntime().addShutdownHook(new Thread(this.consumer::wakeup));
        }

        return consumer;
    }

    protected abstract void handleRecords(ConsumerRecords<String, T> records);

    protected abstract void configureConsumer(Properties config);

    protected abstract List<String> getTopics();

    @Override
    public void run() {
        Consumer<String, T> consumer = this.getConsumer();

        try {
            consumer.subscribe(this.getTopics());

            while (true) {
                ConsumerRecords<String, T> records = consumer.poll(Duration.ofSeconds(5));
                this.handleRecords(records);
            }
        } catch (WakeupException ignore) {
        } finally {
            try {
                consumer.commitSync();
            } finally {
                log.info("Closing consumer");
                consumer.close();
            }
        }
    }
}
