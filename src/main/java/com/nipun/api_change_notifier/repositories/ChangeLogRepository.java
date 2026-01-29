package com.nipun.api_change_notifier.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nipun.api_change_notifier.models.ChangeLog;

@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLog, UUID> {
    
}
