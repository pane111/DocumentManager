package com.fhtw.rest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fhtw.rest.RabbitMQConfig;
import com.fhtw.rest.dto.DocumentDto;
import com.fhtw.rest.dto.MessageContainer;
import com.fhtw.rest.mapper.DocumentMapper;
import com.fhtw.rest.model.Document;
import com.fhtw.rest.repository.DocumentRepo;
import io.minio.*;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;


@Service
@Log
public class DocumentService {
    private final DocumentRepo repo;

    private final RabbitTemplate rabbitTemplate;

    public DocumentService(DocumentRepo repo, RabbitTemplate rabbitTemplate) {
        this.repo = repo;
        this.rabbitTemplate = rabbitTemplate;
    }

    public DocumentDto create(DocumentDto dto) {
        Document d = DocumentMapper.dtoToDoc(dto);
        Document saved = repo.save(d);

        /*
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(saved);
            //rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, json);
            log.info("Sent document to RabbitMQ");
        }
        catch (Exception e) {
            log.warning(e.getMessage());
        }
        */
        return DocumentMapper.docToDto(saved);
    }

    public void uploadFile(DocumentDto dto,MultipartFile file) throws Exception {
        log.info("Uploading file to MinIO");


        MinioClient minioClient = MinioClient.builder()
                .endpoint(System.getenv("MINIO_ENDPOINT"))
                .credentials(System.getenv("MINIO_ACCESS_KEY"), System.getenv("MINIO_SECRET_KEY"))
                .build();

        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("documents").build());
        if (!found) {
            log.warning("Bucket does not exist. Creating bucket ");
            minioClient.makeBucket(MakeBucketArgs.builder().bucket("documents").build());
        }


        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("documents")
                        .object(file.getOriginalFilename())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
        log.info("Upload finished!");

        log.info("Creating document on database");
        DocumentDto temp = create(dto);
        log.info("Temp ID: " + temp.getId());
        MessageContainer mcontainer = new MessageContainer();
        mcontainer.setId(temp.getId());
        mcontainer.setFilepath(file.getOriginalFilename());

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, new ObjectMapper().writeValueAsString(mcontainer));
    }

    public List<DocumentDto> findAll() {
        List<DocumentDto> dtos = repo.findAll().stream().map(DocumentMapper::docToDto).toList();
        log.info("Retrieved documents:\n"+dtos.toString());
        return dtos;
    }


    public DocumentDto findById(Long id) {
        return repo.findById(id).map(DocumentMapper::docToDto).orElse(null);
    }


    public DocumentDto update(Long id, DocumentDto dto) {
        return repo.findById(id).map(existing -> {
            existing.setTitle(dto.getTitle());
            existing.setContent(dto.getContent());
            Document updated = repo.save(existing);
            return DocumentMapper.docToDto(updated);
        }).orElse(null);
    }


    @RabbitListener(queues = RabbitMQConfig.CONFIRM_QUEUE)
    public void updateStatus(String msg) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            MessageContainer mcontainer = mapper.readValue(msg, MessageContainer.class);

            Long id = mcontainer.getId();
            String path = mcontainer.getFilepath();
            String summary = mcontainer.getMessage();
            log.info("Updating document status of "+id);
            Document doc = repo.findById(id).get();
            doc.setStatus("processed");
            doc.setPath(path);
            doc.setContent(summary);
            repo.save(doc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
