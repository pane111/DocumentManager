package com.fhtw.rest.dto;
import lombok.*;


@Data
public class DocumentDto {
    private Long id;
    private String title;
    private String content;
    private String status;

    //These getters and setters are required due to a bug with lombok
    public DocumentDto() {

    }


    public DocumentDto(Long id, String title, String content, String status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    // Getter & Setter for title
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter & Setter for content
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
