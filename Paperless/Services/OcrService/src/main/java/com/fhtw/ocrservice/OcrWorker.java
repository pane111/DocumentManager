package com.fhtw.ocrservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fhtw.ocrservice.model.Document;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.nio.channels.Channel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
        MinioClient client = MinioClient.builder()
                .endpoint(System.getenv("MINIO_ENDPOINT"))
                .credentials(System.getenv("MINIO_ACCESS_KEY"), System.getenv("MINIO_SECRET_KEY"))
                .build();

        InputStream is = client.getObject(
                GetObjectArgs.builder()
                        .bucket("documents")
                        .object(msg)
                        .build()
        );
        if (is != null) {
            log.info("File found on MinIO " + is.toString());
        }

        File tempFile = File.createTempFile("ocr-", ".pdf");
        Files.copy(is, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Tesseract tesseract = new Tesseract();
        try {
            tesseract.setDatapath("/usr/share/tesseract-ocr/5/tessdata/");
            tesseract.setLanguage("eng");
            String result = tesseract.doOCR(tempFile);
            log.info("Tesseract result: " + result);
            log.info("Sending document content to Gemini Worker...");
            rabbitTemplate.convertAndSend(RabbitMQConfig.GEMINI_QUEUE, result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tempFile.delete();
        }




        /*
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

        */


    }

}
