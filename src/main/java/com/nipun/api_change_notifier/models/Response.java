package com.nipun.api_change_notifier.models;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response extends BaseEntity {

    int statusCode = 0;
    Map<String, Object> body;
    Map<String, String> headers;

}
