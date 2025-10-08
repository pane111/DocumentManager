package com.fhtw.rest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fhtw.rest.RabbitMQConfig;
import com.fhtw.rest.dto.DocumentDto;
import com.fhtw.rest.mapper.DocumentMapper;
import com.fhtw.rest.model.Document;
import com.fhtw.rest.repository.DocumentRepo;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


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
        ObjectMapper mapper = new ObjectMapper();

        try {
            String json = mapper.writeValueAsString(saved);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, json);
            log.info("Sent document to RabbitMQ");
        }
        catch (Exception e) {
            log.warning(e.getMessage());
        }

        return DocumentMapper.docToDto(saved);
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
    public void updateStatus(String id) {
        log.info("Updating document status of "+id);
        Document doc = repo.findById(Long.parseLong(id)).get();
        doc.setStatus("processed");
        repo.save(doc);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
