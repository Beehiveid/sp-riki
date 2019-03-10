package com.pancabudi.technic.complaintType;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ComplaintTypeRepository extends DataTablesRepository<ComplaintType, Long> {
    ComplaintType findByName(String name);

    @Nullable
    List<ComplaintType> findAll();

    boolean existsByName(String type);
}
