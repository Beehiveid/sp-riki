package com.beehive.riki.solutionType;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface SolutionTypeRepository extends DataTablesRepository<SolutionType, Long> {
    @Nullable
    List<SolutionType> findAll();
}
