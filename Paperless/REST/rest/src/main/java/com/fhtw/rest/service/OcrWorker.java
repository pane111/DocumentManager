package com.fhtw.rest.service;


import com.fhtw.rest.RabbitMQConfig;
import com.fhtw.rest.model.Document;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.nio.channels.Channel;

@Service
@Log
public class OcrWorker {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void confirm(String msg) throws Exception {
        log.info("OCR Worker received: " +msg +  " thread: " + Thread.currentThread().getName());
    }

}
