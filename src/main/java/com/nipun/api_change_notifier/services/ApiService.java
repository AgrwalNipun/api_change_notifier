package com.nipun.api_change_notifier.services;

import org.springframework.stereotype.Service;

import com.nipun.api_change_notifier.models.Api;

@Service
public class ApiService {

    public Api saveApi(Api api) {

        if (api.getEndPoint() == null) {
            throw new IllegalArgumentException("Endpoint cannot be null");
        }

        System.out.println("---------- API ----------");
        System.out.println("Method    : " + api.getMethod());
        System.out.println("Endpoint  : " + api.getEndPoint());
        System.out.println("Payload   : " + api.getPayload());
        System.out.println("Response  : " + api.getResponse());

        return api;
    }

}
