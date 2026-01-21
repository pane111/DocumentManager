package com.fhtw.ocrservice;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fhtw.ocrservice.model.MessageContainer;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Log
public class GeminiWorker {

    private final RabbitTemplate rabbitTemplate;
    private final String apiKey = System.getenv("GEMINI_API_KEY");

    public GeminiWorker(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.GEMINI_QUEUE)
    public void summarize(String messageCont) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MessageContainer mc = mapper.readValue(messageCont, MessageContainer.class);
        String ocrText = mc.getMessage();
        log.info("Gemini API Key: " + apiKey);
        log.info("File received: " +mc.getFilepath() +", Document ID: " + mc.getId());
        log.info("Creating request...");

        String prompt = """
                Summarize the following document in one paragraph. Respond only with the summary text and nothing else. Use at most 1000 characters.
                
                """+ocrText;

        String response = geminiResponse(mc.getFilepath(),prompt);
        //String response = "THIS IS ONLY A TEST. TEST KEYWORD: Umbrella";
        if (response == null)
        {
            log.warning("Response from Gemini was NULL!");
            MessageContainer finalResponse = new MessageContainer(mc.getFilepath(),"Gemini failed to generate a summary.");
            finalResponse.setId(mc.getId());
            rabbitTemplate.convertAndSend(RabbitMQConfig.INDEX_QUEUE, new ObjectMapper().writeValueAsString(finalResponse));
            return;
        }
        log.info("Worker received final string:" + response);
        MessageContainer finalResponse = new MessageContainer(mc.getFilepath(),response);
        finalResponse.setId(mc.getId());
        String msg = mapper.writeValueAsString(finalResponse);
        rabbitTemplate.convertAndSend(RabbitMQConfig.INDEX_QUEUE, msg);
    }

    private String geminiResponse(String path,String prompt) throws Exception {
        String jsonBody = """
                {
                  "contents": [
                    {
                      "parts": [
                        { "text": "%s" }
                      ]
                    }
                  ]
                }
                """.formatted(prompt);
        log.info("Calling gemini: " + jsonBody);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent"))
                .header("Content-Type","application/json")
                .header("X-goog-api-key",apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Status code: " + response.statusCode());
            log.info(response.body());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            String summary = root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            return summary;

        }
        catch (Exception e) {
            e.printStackTrace();
            return "Gemini failed to generate a summary. Reason: " + e.getMessage();
        }
    }

}
