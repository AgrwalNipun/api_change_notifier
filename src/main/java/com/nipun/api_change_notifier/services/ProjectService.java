package com.nipun.api_change_notifier.services;

import org.springframework.stereotype.Service;

import com.nipun.api_change_notifier.models.Project;

@Service
public class ProjectService {

    public Project saveProject(Project project) {
        if (project.getName() == null)
            throw new IllegalArgumentException("Cannot create a project without name");

        System.out.println("---------- PROJECT ----------");
        System.out.println("Name      : " + project.getName());
        System.out.println("Github ID : " + project.getGithubId());
        System.out.println("APIs      : " + project.getApis());

        return project;
    }

    public Project projectExists(String githubId) {
        System.out.println("---------- PROJECT EXISTS CHECK ----------");
        System.out.println("Checking for Github ID: " + githubId);
        // No DB — return an empty Project
        return new Project();
    }

}
