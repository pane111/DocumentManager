package com.fhtw.rest.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comments",schema="dm_schema")
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long docId;

    private String author;
    private String content;
}
