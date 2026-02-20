package com.nipun.api_change_notifier.models;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payload extends BaseEntity {

    Map<String, String> headers;
    Map<String, Object> body;
    Map<String, Object> params;

}
