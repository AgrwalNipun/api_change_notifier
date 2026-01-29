package com.nipun.api_change_notifier.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nipun.api_change_notifier.models.ChangeLog;
import com.nipun.api_change_notifier.repositories.ChangeLogRepository;

@Service
public class ChangeLogService {

    @Autowired
    private ChangeLogRepository repo;

    public ChangeLog saveChangeLog(ChangeLog changeLog) {
        return repo.save(changeLog);
    }
}
