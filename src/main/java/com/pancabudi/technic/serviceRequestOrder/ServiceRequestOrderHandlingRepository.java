package com.pancabudi.technic.serviceRequestOrder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRequestOrderHandlingRepository extends JpaRepository<ServiceRequestOrderHandling, String> {
    ServiceRequestOrderHandling findTop1ByServiceRequestOrderIdOrderBySystemDateDesc(String sroId);

    List<ServiceRequestOrderHandling> findByServiceRequestOrderId(String id);
}
