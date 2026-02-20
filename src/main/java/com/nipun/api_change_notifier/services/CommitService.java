package com.nipun.api_change_notifier.services;

import org.springframework.stereotype.Service;

import com.nipun.api_change_notifier.models.Commit;

@Service
public class CommitService {

    public Commit saveCommit(Commit commit) {
        System.out.println("---------- COMMIT ----------");
        System.out.println("Commit : " + commit);
        return commit;
    }

}
