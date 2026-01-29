package com.nipun.api_change_notifier.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nipun.api_change_notifier.models.Api;
import com.nipun.api_change_notifier.repositories.ApiRepository;

@Service
public class ApiService {
    
    @Autowired
    private ApiRepository repo;

    public Api saveApi(Api api){
        
        if (api.getEndPoint() == null) {
            throw new IllegalArgumentException("Endpoint cannot be null");
        }

        
        return repo.save(api);


    }

}
