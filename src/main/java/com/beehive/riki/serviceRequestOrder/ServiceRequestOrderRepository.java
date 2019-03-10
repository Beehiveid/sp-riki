package com.beehive.riki.serviceRequestOrder;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;

public interface ServiceRequestOrderRepository extends DataTablesRepository<ServiceRequestOrder, String> {
    ServiceRequestOrder findTop1ByCreatedTimeBetweenOrderByCreatedTimeDesc(Date min, Date max);

    @Nullable
    List<ServiceRequestOrder> findAll();
}
