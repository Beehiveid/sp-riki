package com.beehive.riki.system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SystemEnvironmentRepository extends JpaRepository<SystemEnvironment, Long>, JpaSpecificationExecutor<SystemEnvironment> {
}
