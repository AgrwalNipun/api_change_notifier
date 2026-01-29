package com.nipun.api_change_notifier.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nipun.api_change_notifier.models.Payload;
import com.nipun.api_change_notifier.repositories.PayloadRepository;

@Service
public class PayloadService {
    
    @Autowired
    private PayloadRepository repo;


    public Payload savePayload(Payload payload){

        if(payload.getBody() == null)throw new IllegalArgumentException("Payload body cant be null");
        return repo.save(payload);
    }

}
