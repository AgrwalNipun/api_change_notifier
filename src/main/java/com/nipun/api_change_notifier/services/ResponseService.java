package com.nipun.api_change_notifier.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nipun.api_change_notifier.models.Response;
import com.nipun.api_change_notifier.repositories.ResponseRepository;

@Service
public class ResponseService {

    @Autowired
    private ResponseRepository repo;

    public Response saveResponse(Response response) {

        return repo.save(response);

    }

}
