package com.nipun.api_change_notifier.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;



@Entity
public class Commit extends BaseEntity {

    

    @ManyToOne
    @JoinColumn(name = "project_id")
    Project project;

    String author;
    String commitName;
    LocalDateTime commitedAt;
    
}
