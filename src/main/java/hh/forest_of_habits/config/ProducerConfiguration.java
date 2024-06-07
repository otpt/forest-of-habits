package hh.forest_of_habits.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hh.forest_of_habits.model.Message;
import hh.forest_of_habits.service.DataSender;
import hh.forest_of_habits.service.impl.DataSenderKafka;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class ProducerConfiguration {
    private static final Integer PARTITIONS_COUNT = 1;
    private static final Integer REPLICAS_COUNT = 1;

    private static final Logger log = LoggerFactory.getLogger(ProducerConfiguration.class);
    public final String topicName;

    public ProducerConfiguration(@Value("${application.kafka.topic}") String topicName) {
        this.topicName = topicName;
    }

    @Bean
    public ProducerFactory<String, Message> producerFactory(
            KafkaProperties kafkaProperties, ObjectMapper objectMapper) {
        var props = kafkaProperties.buildProducerProperties(null);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        var kafkaProducerFactory = new DefaultKafkaProducerFactory<String, Message>(props);
        kafkaProducerFactory.setValueSerializer(new JsonSerializer<>(objectMapper));
        return kafkaProducerFactory;
    }

    @Bean
    public KafkaTemplate<String, Message> kafkaTemplate(
            ProducerFactory<String, Message> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder
                .name(topicName)
                .partitions(PARTITIONS_COUNT)
                .replicas(REPLICAS_COUNT)
                .build();
    }

    @Bean
    public DataSender dataSender(NewTopic topic, KafkaTemplate<String, Message> kafkaTemplate) {
        return new DataSenderKafka(
                topic.name(),
                kafkaTemplate,
                stringValue -> log.info("producer, value: {}", stringValue));
    }
}
