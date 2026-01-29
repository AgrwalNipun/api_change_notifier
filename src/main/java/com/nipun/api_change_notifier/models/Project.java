package com.nipun.api_change_notifier.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity
public class Project extends BaseEntity{
    

    String name;
    
    @ElementCollection
    @CollectionTable(
        name = "project_users",
        joinColumns = @JoinColumn(name = "project_id")
    )
    @Column(name = "email")
    List<String> usersEmail;


    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Commit> commits;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Api> apis;



}
