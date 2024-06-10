package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.model.Message;
import hh.forest_of_habits.service.DataSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.function.Consumer;

public class DataSenderKafka implements DataSender {
    private static final Logger log = LoggerFactory.getLogger(DataSenderKafka.class);

    private final KafkaTemplate<String, Message> template;

    private final Consumer<Message> consumer;

    private final String topic;

    public DataSenderKafka(
            String topic,
            KafkaTemplate<String, Message> template,
            Consumer<Message> consumer) {
        this.topic = topic;
        this.template = template;
        this.consumer = consumer;
    }

    @Override
    public void send(Message value) {
        try {
            log.info("Sending: {}", value);
            template.send(topic, value)
                    .whenComplete(
                            (result, e) -> {
                                if (e == null) {
                                    log.info("сообщение с id: {} успешно отправлено, offset: {}",
                                            value.id(),
                                            result.getRecordMetadata().offset()
                                    );
                                    consumer.accept(value);
                                } else {
                                    log.error("сообщение с id: {} не отправлено", value.id(), e);
                                }
                            });

        } catch (Exception e) {
            log.error("ошибка отправки, сообщение: {}", value, e);
        }
    }
}
