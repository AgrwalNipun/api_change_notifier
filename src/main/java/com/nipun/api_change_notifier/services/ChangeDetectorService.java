package com.nipun.api_change_notifier.services;

import java.io.File;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nipun.api_change_notifier.models.Api;
import com.nipun.api_change_notifier.models.ChangeLog;
import com.nipun.api_change_notifier.models.Payload;
import com.nipun.api_change_notifier.models.Project;
import com.nipun.api_change_notifier.models.Response;
import com.nipun.api_change_notifier.models.enums.Status;

@Service
public class ChangeDetectorService {

    @Autowired
    private FileIteratingService fileIteratingService;


    public List<ChangeLog> getChanges() {      

        File projectDir = new File("C:\\Users\\nipun\\Desktop\\spring boot\\roadmap2");
        File projectDir2 = new File("C:\\Users\\nipun\\Desktop\\roadmap2");

        Project oldProject = fileIteratingService.processProject(projectDir);
        Project newProject = fileIteratingService.processProject(projectDir2);

        List<ChangeLog> changes = new ArrayList<>();

        Map<String, Api> oldApiMap = mapApis(oldProject.getApis());
        Map<String, Api> newApiMap = mapApis(newProject.getApis());

        // Added & modified APIs
        for (String key : newApiMap.keySet()) {
            Api newApi = newApiMap.get(key);

            if (!oldApiMap.containsKey(key)) {
                ChangeLog log = new ChangeLog();
                log.setStatus(Status.ADDED);
                log.setApi(newApi);
                log.setPastValue(null);
                log.setCurrentValue(Map.of(
                        "method", newApi.getMethod(),
                        "path", newApi.getEndPoint()
                ));
                changes.add(log);
            } else {
                compareApis(oldApiMap.get(key), newApi, changes);
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
                        "path", oldApi.getEndPoint()
                ));
                log.setCurrentValue(null);
                changes.add(log);
            }
        }

        return changes;
    }

    /* ------------------- API LEVEL ------------------- */

    private void compareApis(Api oldApi, Api newApi, List<ChangeLog> changes) {

        compareScope(
                oldApi.getPayload(),
                newApi.getPayload(),
                "REQUEST",
                oldApi,
                changes
        );

        compareScope(
                oldApi.getResponse(),
                newApi.getResponse(),
                "RESPONSE",
                oldApi,
                changes
        );
    }

    /* ------------------- SCOPE LEVEL (AGGREGATED) ------------------- */

    private void compareScope(
            Object oldObj,
            Object newObj,
            String scope,
            Api api,
            List<ChangeLog> changes) {

        Map<String, Object> oldMap = safeMap(oldObj);
        Map<String, Object> newMap = safeMap(newObj);

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

        // Emit max 3 logs per scope
        if (!added.isEmpty()) {
            changes.add(buildLog(Status.ADDED, api, null, wrap(scope, added)));
        }

        if (!removed.isEmpty()) {
            changes.add(buildLog(Status.REMOVED, api, wrap(scope, removed), null));
        }

        if (!modifiedOld.isEmpty()) {
            changes.add(buildLog(
                    Status.CHANGED,
                    api,
                    wrap(scope, modifiedOld),
                    wrap(scope, modifiedNew)
            ));
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
            map.put(api.getMethod() + " " + api.getEndPoint(), api);
        }
        return map;
    }

    private Map<String, Object> safeMap(Object obj) {
        if (obj instanceof Payload p && p.getBody() != null) {
            return p.getBody();
        }
        if (obj instanceof Response r && r.getBody() != null) {
            return r.getBody();
        }
        return Collections.emptyMap();
    }
}
