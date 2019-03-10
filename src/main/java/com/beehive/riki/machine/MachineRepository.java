package com.beehive.riki.machine;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.lang.Nullable;

import java.util.List;

interface MachineRepository extends DataTablesRepository<Machine, Long> {
    @Override
    @Nullable
    List<Machine> findAll();
}
