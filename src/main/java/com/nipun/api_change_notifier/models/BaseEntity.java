package com.nipun.api_change_notifier.models;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseEntity {

    UUID id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
