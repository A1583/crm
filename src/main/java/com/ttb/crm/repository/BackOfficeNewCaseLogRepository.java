package com.ttb.crm.repository;

import com.ttb.crm.repository.entity.BackOfficeNewCaseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackOfficeNewCaseLogRepository extends JpaRepository<BackOfficeNewCaseLog, Long> {
}
