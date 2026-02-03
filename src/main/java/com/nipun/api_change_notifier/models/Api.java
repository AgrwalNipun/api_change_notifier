package com.nipun.api_change_notifier.models;

import java.util.List;
import com.nipun.api_change_notifier.models.enums.Method;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "apis")
public class Api extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "project_id")
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

    @Column(nullable = false)
    String endPoint;

    @OneToMany(mappedBy = "api", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ChangeLog> changes;

    @Override
    public String toString() {
        return "Api [method=" + method + ", endPoint=" + endPoint + "]";
    }

}
