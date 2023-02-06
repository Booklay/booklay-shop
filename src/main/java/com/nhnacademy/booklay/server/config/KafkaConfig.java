package com.nhnacademy.booklay.server.config;

import com.nhnacademy.booklay.server.dto.coupon.message.CouponIssueRequestMessage;
import com.nhnacademy.booklay.server.dto.coupon.message.CouponIssueResponseMessage;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@EnableKafka
@Configuration
public class KafkaConfig {

    public ConsumerFactory<String, CouponIssueResponseMessage> consumerFactory(){
        JsonDeserializer<CouponIssueResponseMessage> jsonDeserializer =
            new JsonDeserializer<>(CouponIssueResponseMessage.class);

        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setUseTypeMapperForKey(true);

        ErrorHandlingDeserializer<CouponIssueResponseMessage> deserializer = new ErrorHandlingDeserializer<>(jsonDeserializer);

        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "133.186.241.189:9092, 133.186.219.132:9092, 133.186.214.24:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "shop");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CouponIssueResponseMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CouponIssueResponseMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ProducerFactory<String, CouponIssueRequestMessage> producerFactory() {
        return new DefaultKafkaProducerFactory<>(senderProps());
    }

    private Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "133.186.241.189:9092, 133.186.219.132:9092, 133.186.214.24:9092");
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return props;
    }

    @Bean
    public KafkaTemplate<String, CouponIssueRequestMessage> kafkaTemplate(ProducerFactory<String, CouponIssueRequestMessage> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}