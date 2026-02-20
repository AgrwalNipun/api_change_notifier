package com.nipun.api_change_notifier.services;

import org.springframework.stereotype.Service;

import com.nipun.api_change_notifier.models.Response;

@Service
public class ResponseService {

    public Response saveResponse(Response response) {
        System.out.println("---------- RESPONSE ----------");
        System.out.println("Status Code : " + response.getStatusCode());
        System.out.println("Body        : " + response.getBody());
        System.out.println("Headers     : " + response.getHeaders());
        return response;
    }

}
