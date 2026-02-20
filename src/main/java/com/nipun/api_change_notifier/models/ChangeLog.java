package com.nipun.api_change_notifier.models;

import java.util.Map;

import com.nipun.api_change_notifier.models.enums.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeLog extends BaseEntity {

    String pastCommitId;
    String currentCommitId;
    Status status;
    Api api;
    Map<String, Object> pastValue;
    Map<String, Object> currentValue;

    @Override
    public String toString() {
        return "ChangeLog [pastCommitId=" + pastCommitId + ", currentCommitId=" + currentCommitId
                + ", status=" + status + ", api=" + api.toString()
                + ", pastValue=" + pastValue + ", currentValue=" + currentValue + "]";
    }

}
