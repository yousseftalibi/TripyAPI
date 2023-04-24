package com.isep.trippy.Services;
import com.isep.trippy.Models.Place;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service

public class PlaceService {

        @Autowired
        private KafkaTemplate<String, String> kafkaTemplate;

        public void sendMessage(String message){
            kafkaTemplate.send("my-topic", message);
        }

        @KafkaListener(topics = "my-topic-uppercase", groupId = "my-group")
        public void listenUppercase(ConsumerRecord<String, String> record){
            System.out.println("received uppercase message: "+ record.value() );
        }

        public List<Place> cleanData(List<Place> places){
            sendMessage("cleaning data currently....");
            RestTemplate restTemplate = new RestTemplate();
            String uri = "http://localhost:5000/cluster_data";
            List<String> duplicatePlaces = new ArrayList<>();
            for (Place p:
                    places) {
                duplicatePlaces.add(p.getName());
            }
            HttpHeaders headers = new HttpHeaders();

            HttpEntity<List<String>> requestEntity = new HttpEntity<>(duplicatePlaces, headers);
            ResponseEntity<List> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, List.class);


            List<Place> uniquePlaces = new ArrayList<>();

            for (Object distinctName : response.getBody()) {
                if (distinctName.toString().trim().isEmpty()) {
                    continue;
                }
                for (Place place : places) {
                    String placeNameNormalized = place.getName().trim().toLowerCase();
                    String distinctNameNormalized = distinctName.toString().trim().toLowerCase();

                    if (placeNameNormalized.isEmpty() || distinctNameNormalized.isEmpty()) {
                        continue;
                    }

                    if (placeNameNormalized.equals(distinctNameNormalized) || placeNameNormalized.contains(distinctNameNormalized) || distinctNameNormalized.contains(placeNameNormalized)) {
                        uniquePlaces.add(place);
                        break;
                    }
                }
            }
           sendMessage("data cleaned.");

            return uniquePlaces;
        }



}
