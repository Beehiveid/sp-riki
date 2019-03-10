package com.beehive.riki.agency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AgencyRepository extends JpaRepository<Agency, Long>, JpaSpecificationExecutor<Agency> {
}
