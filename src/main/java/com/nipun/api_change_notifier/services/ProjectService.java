package com.nipun.api_change_notifier.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nipun.api_change_notifier.models.Project;
import com.nipun.api_change_notifier.repositories.ProjectRepository;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository repo;

    public Project saveProject(Project project) {
        if (project.getName() == null)
            throw new IllegalArgumentException("Cannot create a project without name");

        return repo.save(project);
    }

    public Project projectExists(String githubId) {

        Project project = repo.findByGithubId("123456789")
                .orElse(new Project());

        return project;
    }

}
