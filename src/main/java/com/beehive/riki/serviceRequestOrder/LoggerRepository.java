package com.beehive.riki.serviceRequestOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LoggerRepository extends JpaRepository<Logger, Long>, JpaSpecificationExecutor<Logger> {
}
