package com.fhtw.rest.service;

import com.fhtw.rest.dto.DocumentDto;
import com.fhtw.rest.mapper.DocumentMapper;
import com.fhtw.rest.model.Document;
import com.fhtw.rest.repository.DocumentRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImp implements DocumentService {
    private final DocumentRepo repo;
    public DocumentServiceImp(DocumentRepo repo) {
        this.repo = repo;
    }
    Logger logger = LogManager.getLogger(DocumentServiceImp.class);
    @Override
    public DocumentDto create(DocumentDto dto) {
        Document d = DocumentMapper.dtoToDoc(dto);
        Document saved = repo.save(d);
        return DocumentMapper.docToDto(saved);
    }

    @Override
    public List<DocumentDto> findAll() {
        List<DocumentDto> dtos = repo.findAll().stream().map(DocumentMapper::docToDto).toList();
        logger.info("Retrieved documents:\n"+dtos.toString());
        return dtos;
    }

    @Override
    public DocumentDto findById(Long id) {
        return repo.findById(id).map(DocumentMapper::docToDto).orElse(null);
    }

    @Override
    public DocumentDto update(Long id, DocumentDto dto) {
        return repo.findById(id).map(existing -> {
            existing.setTitle(dto.getTitle());
            existing.setContent(dto.getContent());
            Document updated = repo.save(existing);
            return DocumentMapper.docToDto(updated);
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
