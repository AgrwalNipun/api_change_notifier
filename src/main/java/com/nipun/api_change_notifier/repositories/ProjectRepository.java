package com.nipun.api_change_notifier.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nipun.api_change_notifier.models.Project;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProjectRepository extends JpaRepository<Project,UUID>{   


    public Optional<Project> findByGithubId(String githubId);

}
