package com.beehive.riki.salesPerson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SalesPersonRepository extends JpaRepository<SalesPerson, Long>, JpaSpecificationExecutor<SalesPerson> {
}
