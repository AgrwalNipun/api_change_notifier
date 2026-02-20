package com.nipun.api_change_notifier.models;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Commit extends BaseEntity {

    Project project;
    String author;
    String commitName;
    LocalDateTime commitedAt;

}
