package com.nipun.api_change_notifier.models;

import java.util.List;

import com.nipun.api_change_notifier.models.enums.Method;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Api extends BaseEntity {

    Method method;
    Payload payload;
    Response response;
    @JsonIgnore
    Commit commit;
    String endPoint;
    List<ChangeLog> changes;

    @Override
    public String toString() {
        return "Api [method=" + method + ", endPoint=" + endPoint + "]";
    }

}
