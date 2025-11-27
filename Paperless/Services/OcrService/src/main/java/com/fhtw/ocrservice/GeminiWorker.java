package com.fhtw.ocrservice;


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
    public void summarize(String ocrText) throws Exception {
        log.info("Gemini API Key: " + apiKey);
        log.info("Creating request...");

        String prompt = """
                Summarize the following document in one paragraph. Respond only with the summary text and nothing else.
                
                """+ocrText;

        String response = geminiResponse(prompt);
        if (response == null)
        {
            log.warning("Response from Gemini was NULL!");
            return;
        }
        log.info("Worker received final string:" + response);
    }

    private String geminiResponse(String prompt) throws Exception {
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
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"))
                .header("Content-Type","application/json")
                .header("X-goog-api-key",apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Status code: " + response.statusCode());
            log.info(response.body());
            return response.body();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
