package ru.mfp.common.config

import java.util.UUID
import java.util.function.Supplier
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.common.serialization.UUIDDeserializer
import org.apache.kafka.common.serialization.UUIDSerializer
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.context.properties.PropertyMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.LoggingProducerListener
import org.springframework.kafka.support.ProducerListener
import org.springframework.kafka.support.converter.RecordMessageConverter

@Configuration
class KafkaConfig(
    private val properties: KafkaProperties
) {

    @Bean
    fun kafkaProducerListener(): ProducerListener<UUID, String> {
        return LoggingProducerListener()
    }

    @Bean
    fun kafkaTemplate(
        kafkaProducerFactory: ProducerFactory<UUID, String>,
        kafkaProducerListener: ProducerListener<UUID, String>,
        messageConverter: ObjectProvider<RecordMessageConverter>
    ): KafkaTemplate<UUID, String> {
        val kafkaTemplate = KafkaTemplate(kafkaProducerFactory)
        messageConverter.ifUnique { kafkaTemplate.setMessageConverter(it) }
        val map = PropertyMapper.get().alwaysApplyingWhenNonNull()
        map.from(kafkaProducerListener).to { kafkaTemplate.setProducerListener(it) }
        map.from(properties.template.defaultTopic).to { kafkaTemplate.setDefaultTopic(it) }
        map.from(properties.template.transactionIdPrefix).to { kafkaTemplate.setTransactionIdPrefix(it) }
        return kafkaTemplate
    }

    @Bean
    fun kafkaConsumerFactory(customizers: ObjectProvider<DefaultKafkaConsumerFactoryCustomizer>): DefaultKafkaConsumerFactory<UUID, String> {
        val factory = DefaultKafkaConsumerFactory(
            properties.buildConsumerProperties(),
            Supplier<Deserializer<UUID>> { UUIDDeserializer() },
            Supplier<Deserializer<String>> { StringDeserializer() }
        )
        customizers.orderedStream().forEach { it.customize(factory) }
        return factory
    }

    @Bean
    fun kafkaProducerFactory(customizers: ObjectProvider<DefaultKafkaProducerFactoryCustomizer>): DefaultKafkaProducerFactory<UUID, String> {
        val factory = DefaultKafkaProducerFactory(
            properties.buildProducerProperties(),
            Supplier<Serializer<UUID>> { UUIDSerializer() },
            Supplier<Serializer<String>> { StringSerializer() })
        val transactionIdPrefix = properties.producer.transactionIdPrefix
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix)
        }
        customizers.orderedStream().forEach { it.customize(factory) }
        return factory
    }
}

