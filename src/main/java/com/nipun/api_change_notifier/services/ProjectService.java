package com.nipun.api_change_notifier.services;

import org.springframework.stereotype.Service;

import com.nipun.api_change_notifier.models.Project;

@Service
public class ProjectService {

    public Project saveProject(Project project) {
        return project;
    }

    public Project projectExists(String githubId) {
        System.out.println("---------- PROJECT EXISTS CHECK ----------");
        System.out.println("Checking for Github ID: " + githubId);
        // No DB — return an empty Project
        return new Project();
    }

}
