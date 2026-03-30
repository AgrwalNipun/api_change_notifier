package com.nipun.api_change_notifier.models;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Commit extends BaseEntity {

    @com.fasterxml.jackson.annotation.JsonIgnore
    Project project;
    String author;
    String commitName;
    String commitId;
    LocalDateTime commitedAt;
    List<Api> apis;

}
