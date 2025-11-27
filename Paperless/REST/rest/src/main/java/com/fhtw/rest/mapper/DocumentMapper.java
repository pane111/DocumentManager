package com.fhtw.rest.mapper;

import com.fhtw.rest.dto.DocumentDto;
import com.fhtw.rest.model.Document;

public class DocumentMapper {

    public static DocumentDto docToDto(Document doc) {
        if (doc != null) {
            return new DocumentDto(doc.getId(), doc.getTitle(), doc.getContent(), doc.getStatus());
        }
        return null;
    }

    public static Document dtoToDoc(DocumentDto dto) {
        if (dto!=null) {
            return new Document(dto.getId(), dto.getTitle(), dto.getContent(),dto.getPath());
        }
        return null;
    }
}
