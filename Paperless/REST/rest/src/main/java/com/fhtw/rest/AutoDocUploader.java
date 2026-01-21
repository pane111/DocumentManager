package com.fhtw.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Log
@Component
public class AutoDocUploader implements CommandLineRunner {

    public AutoDocUploader(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
    }

    @Override
    public void run(String... args) throws Exception {
        ClassPathResource resource = new ClassPathResource("UmbrellaHistory.pdf");

        if (resource.exists()) {
            log.info("UmbrellaHistory.pdf exists");
        }
        else {
            log.info("UmbrellaHistory.pdf is missing");
            return;
        }

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("title", "Umbrella History");
        builder.part("file", resource);
        MultiValueMap<String, HttpEntity<?>> body = builder.build();

        RestTemplate restTemplate = new RestTemplate();
        try {
            log.info("Auto-Uploading UmbrellaHistory.pdf");
            String result = restTemplate.postForObject("http://localhost:8080/api/docs", body, String.class);
            log.info("Sent to UmbrellaHistory.pdf");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
