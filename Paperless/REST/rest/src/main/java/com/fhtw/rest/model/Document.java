package com.fhtw.rest.model;
import lombok.*;
import jakarta.persistence.*;
@Entity
@Table(name = "documents",schema="dm_schema")
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String content;

    private String path;

    private String status="processing";

    //These getters and setters are required due to a bug with lombok
    public Document() {

    }

    public Document(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
    public Document(Long id, String title, String content, String path) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.path = path;
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

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

}
