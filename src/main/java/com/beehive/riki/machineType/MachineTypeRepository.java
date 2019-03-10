package com.beehive.riki.machineType;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface MachineTypeRepository extends DataTablesRepository<MachineType, Long> {
    @Nullable
    List<MachineType> findAll();
}
