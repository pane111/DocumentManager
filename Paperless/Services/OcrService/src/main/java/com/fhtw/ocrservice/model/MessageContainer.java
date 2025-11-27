package com.fhtw.ocrservice.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MessageContainer {
    private String filepath;
    private String message;
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFilepath() {
        return filepath;
    }
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public MessageContainer(String filepath, String message) {
        this.filepath = filepath;
        this.message = message;
    }

}
