package com.fhtw.rest.service;

import com.fhtw.rest.dto.DocumentDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DocumentService {
    DocumentDto create(DocumentDto documentDto);
    List<DocumentDto> findAll();
    DocumentDto findById(Long id);
    DocumentDto update(Long id, DocumentDto documentDto);
    void delete(Long id);
}
