package com.nipun.api_change_notifier.services;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nipun.api_change_notifier.models.Api;
import com.nipun.api_change_notifier.models.Project;

@Service
public class FileIteratingService {

    // Inject the service properly via constructor
    private final ParsingService parsingService;
    private final Map<String, String> fileLocations = new HashMap<>();

    @Autowired
    ProjectService projectService;

    public FileIteratingService(ParsingService parsingService) {
        this.parsingService = parsingService;
    }

    public void processProject(File rootDir) {
        List<File> allFiles = scanJavaFiles(rootDir);

        List<Api> apis = new ArrayList<>();

        for (File file : allFiles) {
            apis.addAll(parsingService.parseForControllers(file, fileLocations));
        }

        Project project = new Project();

        project.setApis(apis);
        project.setName("TEST");
        project.setUsersEmail(new ArrayList<>(List.of("nipunagrawal500@gmail.com")));
        for (Api api : apis) {
            api.setProject(project);
        }

        projectService.saveProject(project);
        // System.out.println();

    }

    private List<File> scanJavaFiles(File rootDir) {
        List<File> files = new ArrayList<>();
        if (rootDir == null || !rootDir.exists())
            return files;

        File[] list = rootDir.listFiles();
        if (list == null)
            return files;

        for (File file : list) {
            if (file.isDirectory()) {
                files.addAll(scanJavaFiles(file));
            } else if (file.getName().endsWith(".java")) {
                fileLocations.put(file.getName().replace(".java", ""), file.getAbsolutePath());
                files.add(file);
            }
        }
        return files;
    }

    public Optional<String> returnFileLocationOptional(String className) {
        // Use ofNullable to prevent NullPointerException
        return Optional.ofNullable(fileLocations.get(className));
    }
}