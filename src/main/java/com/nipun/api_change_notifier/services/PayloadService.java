package com.nipun.api_change_notifier.services;

import org.springframework.stereotype.Service;

import com.nipun.api_change_notifier.models.Payload;

@Service
public class PayloadService {

    public Payload savePayload(Payload payload) {

        if (payload.getBody() == null)
            throw new IllegalArgumentException("Payload body cant be null");

        System.out.println("---------- PAYLOAD ----------");
        System.out.println("Body   : " + payload.getBody());
        System.out.println("Params : " + payload.getParams());

        return payload;
    }

}
