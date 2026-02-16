package com.nipun.api_change_notifier.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nipun.api_change_notifier.models.Api;
import com.nipun.api_change_notifier.models.ChangeLog;
import com.nipun.api_change_notifier.models.Project;
import com.nipun.api_change_notifier.models.enums.Status;

@Service
public class ChangeDetectorService {

    @Autowired
    private FileIteratingService fileIteratingService;

  
    /*  public void checkoutToRev(String projectPath, String revision) {
        File workDir = new File(projectPath);
        if (!workDir.exists() || !workDir.isDirectory()) {
            System.err.println("❌ Working directory does not exist or is not a directory: " + projectPath);
            // Debug: list parent directory
            File parent = workDir.getParentFile();
            if (parent != null && parent.exists()) {
                System.out.println("Contents of " + parent.getAbsolutePath() + ":");
                String[] files = parent.list();
                if (files != null) {
                    for (String f : files)
                        System.out.println(" - " + f);
                }
            } else {
                System.err.println("Parent directory " + (parent != null ? parent.getAbsolutePath() : "null")
                        + " also does not exist.");
            }
            return; // Cannot proceed
        }

        try {
            System.out.println("🔄 Checking out to: " + revision + " in " + projectPath);

            // 1. Setup the process to run in the project directory
            ProcessBuilder pb = new ProcessBuilder("git", "checkout", "-f", revision);
            pb.directory(workDir);
            pb.redirectErrorStream(true); // Combine error and info logs

            // 2. Start the process
            Process process = pb.start();

            // 3. Read output
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Git: " + line);
                }
            }

            // 4. Wait for it to finish
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("✅ Successfully checked out to " + revision);
            } else {
                System.err.println("❌ Git checkout failed with exit code: " + exitCode);
            }

        } catch (Exception e) {
            System.err.println("❌ Exception during Git checkout: " + e.getMessage());
            e.printStackTrace();
        }
    }

*/


    public List<ChangeLog> getChanges(String base, String head) {

        // checkoutToRev(path, base);
        File projectDir2 = new File(base);
        Project newProject = fileIteratingService.processProject(projectDir2);

        // checkoutToRev(path, head);
        File projectDir = new File(head);
        Project oldProject = fileIteratingService.processProject(projectDir);

        oldProject.setGithubId("123");
        newProject.setGithubId("456");

        System.out.println("---------- OLD PROJECT ----------");
        printProject(oldProject);

        System.out.println("---------- NEW PROJECT ----------");
        printProject(newProject);

        List<ChangeLog> changes = new ArrayList<>();

        Map<String, Api> oldApiMap = mapApis(oldProject.getApis());
        Map<String, Api> newApiMap = mapApis(newProject.getApis());

        // Added & modified APIs
        for (String key : newApiMap.keySet()) {
            Api newApi = newApiMap.get(key);

            if (oldApiMap.containsKey(key)) {
                compareApis(oldApiMap.get(key), newApi, changes);
            } else {
                ChangeLog log = new ChangeLog();
                log.setStatus(Status.ADDED);
                log.setApi(newApi);
                log.setPastValue(null);
                log.setCurrentValue(Map.of(
                        "method", newApi.getMethod(),
                        "path", newApi.getEndPoint()));
                changes.add(log);
            }
        }

        // Removed APIs
        for (String key : oldApiMap.keySet()) {
            if (!newApiMap.containsKey(key)) {
                Api oldApi = oldApiMap.get(key);

                ChangeLog log = new ChangeLog();
                log.setStatus(Status.REMOVED);
                log.setApi(oldApi);
                log.setPastValue(Map.of(
                        "method", oldApi.getMethod(),
                        "path", oldApi.getEndPoint()));
                log.setCurrentValue(null);
                changes.add(log);
            }
        }

        System.out.println("---------- CHANGES ----------");
        for (ChangeLog log : changes) {
            System.out.println(log.getApi().getEndPoint() + "////////////////");
            ;
            System.out.println(log);
        }

        return changes;
    }

    private void printProject(Project project) {
        System.out.println("Project Name: " + project.getName());
        if (project.getApis() != null) {
            for (Api api : project.getApis()) {
                System.out.println("API: " + api.getMethod() + " " + api.getEndPoint());
                if (api.getPayload() != null) {
                    System.out.println("  Payload: " + api.getPayload().getBody());
                    System.out.println("  Params: " + api.getPayload().getParams());
                }
                if (api.getResponse() != null) {
                    System.out.println("  Response: " + api.getResponse().getBody());
                }
            }
        }
    }

    /* ------------------- API LEVEL ------------------- */

    private void compareApis(Api oldApi, Api newApi, List<ChangeLog> changes) {
        System.out.println("Comparing API: " + oldApi.getMethod() + " " + oldApi.getEndPoint());

        // Compare Request Body
        compareScope(
                oldApi.getPayload() != null ? oldApi.getPayload().getBody() : Collections.emptyMap(),
                newApi.getPayload() != null ? newApi.getPayload().getBody() : Collections.emptyMap(),
                "REQUEST_BODY",
                oldApi,
                changes);

        // Compare Request Params
        compareScope(
                oldApi.getPayload() != null ? oldApi.getPayload().getParams() : Collections.emptyMap(),
                newApi.getPayload() != null ? newApi.getPayload().getParams() : Collections.emptyMap(),
                "REQUEST_PARAMS",
                oldApi,
                changes);

        // Compare Response Body
        compareScope(
                oldApi.getResponse() != null ? oldApi.getResponse().getBody() : Collections.emptyMap(),
                newApi.getResponse() != null ? newApi.getResponse().getBody() : Collections.emptyMap(),
                "RESPONSE_BODY",
                oldApi,
                changes);
    }

    /* ------------------- SCOPE LEVEL (AGGREGATED) ------------------- */

    private void compareScope(
            Map<String, Object> oldMap,
            Map<String, Object> newMap,
            String scope,
            Api api,
            List<ChangeLog> changes) {

        if (oldMap == null)
            oldMap = Collections.emptyMap();
        if (newMap == null)
            newMap = Collections.emptyMap();

        Map<String, Object> added = new HashMap<>();
        Map<String, Object> removed = new HashMap<>();
        Map<String, Object> modifiedOld = new HashMap<>();
        Map<String, Object> modifiedNew = new HashMap<>();

        for (String key : newMap.keySet()) {
            if (!oldMap.containsKey(key)) {
                added.put(key, newMap.get(key));
            }
        }

        for (String key : oldMap.keySet()) {
            if (!newMap.containsKey(key)) {
                removed.put(key, oldMap.get(key));
            }
        }

        for (String key : oldMap.keySet()) {
            if (newMap.containsKey(key)) {
                Object oldVal = oldMap.get(key);
                Object newVal = newMap.get(key);
                if (!Objects.equals(oldVal, newVal)) {
                    modifiedOld.put(key, oldVal);
                    modifiedNew.put(key, newVal);
                }
            }
        }

        // Consolidate all changes into one log if any exist
        if (!added.isEmpty() || !removed.isEmpty() || !modifiedOld.isEmpty()) {
            System.out.println("Changes detected in scope: " + scope + " for API: " + api.getEndPoint());
            Map<String, Object> pastState = new HashMap<>();
            pastState.putAll(removed);
            pastState.putAll(modifiedOld);

            Map<String, Object> currentState = new HashMap<>();
            currentState.putAll(added);
            currentState.putAll(modifiedNew);

            changes.add(buildLog(
                    Status.CHANGED,
                    api,
                    wrap(scope, pastState),
                    wrap(scope, currentState)));
        }
    }

    /* ------------------- HELPERS ------------------- */

    private ChangeLog buildLog(
            Status status,
            Api api,
            Map<String, Object> past,
            Map<String, Object> current) {

        ChangeLog log = new ChangeLog();
        log.setStatus(status);

        log.setApi(api);
        log.setPastValue(past);
        log.setCurrentValue(current);
        return log;
    }

    private Map<String, Object> wrap(String scope, Map<String, Object> data) {
        return Map.of(scope.toLowerCase(), data);
    }

    private Map<String, Api> mapApis(List<Api> apis) {
        Map<String, Api> map = new HashMap<>();
        for (Api api : apis) {
            String method = api.getMethod() != null ? api.getMethod().toString().trim().toUpperCase() : "";
            String endPoint = api.getEndPoint() != null ? api.getEndPoint().trim() : "";
            map.put(method + " " + endPoint, api);
        }
        return map;
    }

}
