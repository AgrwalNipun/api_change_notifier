package com.nipun.api_change_notifier.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nipun.api_change_notifier.models.Api;
import com.nipun.api_change_notifier.models.Project;

@Service
public class NotifierService {

    private static final String TARGET_URL = "http://localhost:8080/api/projects";

    private final RestTemplate restTemplate;

    public NotifierService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void postEndpoints(List<Api> apis, Project project) {
        project.setApis(apis);

        System.out.println("📤 Sending project \"" + project.getName() + "\" with " + apis.size() + " endpoint(s) to "
                + TARGET_URL);

        try {
            String result = restTemplate.postForObject(TARGET_URL, project, String.class);
            System.out.println("✅ Response from server: " + result);
        } catch (Exception e) {
            System.err.println("❌ Failed to POST project: " + e.getMessage());
        }
    }

}
