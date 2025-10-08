package com.fhtw.rest.service;

import com.fhtw.rest.dto.DocumentDto;
import com.fhtw.rest.mapper.DocumentMapper;
import com.fhtw.rest.model.Document;
import com.fhtw.rest.repository.DocumentRepo;
import lombok.extern.java.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Log
public class DocumentService {
    private final DocumentRepo repo;

    public DocumentService(DocumentRepo repo) {
        this.repo = repo;
    }

    public DocumentDto create(DocumentDto dto) {
        Document d = DocumentMapper.dtoToDoc(dto);
        Document saved = repo.save(d);
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


    public void delete(Long id) {
        repo.deleteById(id);
    }
}
