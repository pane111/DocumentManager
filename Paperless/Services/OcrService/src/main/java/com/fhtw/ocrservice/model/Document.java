package com.fhtw.ocrservice.model;

import lombok.Data;

@Data
public class Document {
    private Long id;

    private String title;

    private String content;

    private String status="processing";

    //These getters and setters are required due to a bug with lombok
    public Document() {

    }

    public Document(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // Getter & Setter for id
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

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}
