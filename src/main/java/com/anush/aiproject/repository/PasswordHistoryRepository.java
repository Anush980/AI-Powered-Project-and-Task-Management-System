package com.anush.aiproject.repository;

import java.util.List;

import com.anush.aiproject.entity.PasswordHistory;

public interface PasswordHistoryRepository extends AbstractRepository<PasswordHistory, Long> {

    List<PasswordHistory> findTop5ByUserIdOrderByCreatedAtDesc(Long id);

}
