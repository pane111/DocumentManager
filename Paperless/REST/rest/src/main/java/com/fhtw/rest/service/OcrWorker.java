package com.fhtw.rest.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fhtw.rest.RabbitMQConfig;
import com.fhtw.rest.model.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.channels.Channel;

@Service
@Log
@RequiredArgsConstructor
public class OcrWorker {
    private final RabbitTemplate rabbitTemplate;

    @Async
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void confirm(String msg) throws Exception {
        log.info("OCR Worker received: " +msg +  " thread: " + Thread.currentThread().getName());

        //Process the received data, eg by generating summary

        Thread.sleep(10000); //This is just for testing


        String confirmation = "Processing complete for thread: " + Thread.currentThread().getName() + ", message: " + msg;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Document document = objectMapper.readValue(msg, Document.class);
            rabbitTemplate.convertAndSend(RabbitMQConfig.CONFIRM_QUEUE, document.getId().toString());
            log.info("DONE!");

        }
        catch (Exception e) {
            log.warning("Could not decode document from JSON: " + msg);
            log.severe(e.getMessage());

        }




    }

}
