package com.nipun.api_change_notifier.services;

import org.springframework.stereotype.Service;

import com.nipun.api_change_notifier.models.ChangeLog;

@Service
public class ChangeLogService {

    public ChangeLog saveChangeLog(ChangeLog changeLog) {
        System.out.println("---------- CHANGE LOG ----------");
        System.out.println("Status        : " + changeLog.getStatus());
        System.out
                .println("API           : " + (changeLog.getApi() != null ? changeLog.getApi().getEndPoint() : "null"));
        System.out.println("Past Value    : " + changeLog.getPastValue());
        System.out.println("Current Value : " + changeLog.getCurrentValue());
        return changeLog;
    }

}
