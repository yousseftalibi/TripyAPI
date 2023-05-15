package com.isep.trippy.Services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service

public class PlaceService {
        @Autowired
        private KafkaTemplate<String, String> kafkaTemplate;

       public void sendMessage(String message){
            kafkaTemplate.send("my-topic", message);
        }
        @KafkaListener(topics = "my-topic-uppercase", groupId = "my-group")
        public void listenUppercase(ConsumerRecord<String, String> record){
            System.out.println(record.value() );
        }



}
