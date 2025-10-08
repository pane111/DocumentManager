package com.fhtw.rest;

import com.fhtw.rest.dto.DocumentDto;
import com.fhtw.rest.mapper.DocumentMapper;
import com.fhtw.rest.model.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testDocCreation()
    {
        Document doc = new Document(1L,"New title","Lorem ipsum");
        System.out.println(doc);
        assert doc.getId() == 1L;
        assert doc.getTitle().equals("New title");
        assert doc.getContent().equals("Lorem ipsum");
    }

    @Test
    void testDocConversion()
    {
        Document doc = new Document(1L,"Doc2Dto","Lorem ipsum");
        System.out.println(doc);
        System.out.println("Converting model to dto");
        DocumentDto dto = DocumentMapper.docToDto(doc);
        System.out.println(dto);
        assert dto.getId() == 1L;
        assert dto.getTitle().equals("Doc2Dto");
        assert dto.getContent().equals("Lorem ipsum");
    }

    @Test
    void testDtoConversion()
    {
        DocumentDto dto = new DocumentDto(1L,"Dto2Doc","Lorem","processed");
        System.out.println(dto);
        System.out.println("Converting dto to model");
        Document doc = DocumentMapper.dtoToDoc(dto);
        System.out.println(doc);
        assert doc.getId() == 1L;
        assert doc.getTitle().equals("Dto2Doc");
        assert doc.getContent().equals("Lorem");
    }

}
