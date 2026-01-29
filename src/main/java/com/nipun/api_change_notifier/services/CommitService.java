package com.nipun.api_change_notifier.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nipun.api_change_notifier.models.Commit;
import com.nipun.api_change_notifier.repositories.CommitRepository;

@Service
public class CommitService {

    @Autowired
    private CommitRepository repo;

    public Commit saveCommit(Commit commit) {
        return repo.save(commit);
    }
}
