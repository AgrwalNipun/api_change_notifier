package com.nipun.api_change_notifier.models;

import java.util.List;

import com.nipun.api_change_notifier.models.enums.Method;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;



@Entity
public class Apis extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name="project_id")
    Project project;


    @Enumerated(EnumType.STRING)
    Method method;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payload_id")
    Payload payload;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "response_id")    
    Response response;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commit_id")
    Commit lastCommit;

    String endPoint;


    @OneToMany(mappedBy = "api", cascade = CascadeType.ALL, orphanRemoval = true) 
    List<ChangeLog> changes;

}
