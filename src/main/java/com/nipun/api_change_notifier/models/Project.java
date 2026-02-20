package com.nipun.api_change_notifier.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Project extends BaseEntity {

    String name;
    List<String> usersEmail;
    List<Commit> commits;
    List<Api> apis;
    String githubId;

}
