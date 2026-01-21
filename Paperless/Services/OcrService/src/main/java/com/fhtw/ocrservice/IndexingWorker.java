package com.fhtw.ocrservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Log
@RequiredArgsConstructor
public class IndexingWorker {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = RabbitMQConfig.INDEX_QUEUE)
    public void index(String message) throws Exception {

        log.info("Indexing document...");
        rabbitTemplate.convertAndSend(RabbitMQConfig.CONFIRM_QUEUE, message);
    }
}
