package com.beehive.riki.solutionType;

import com.beehive.riki.common.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolutionTypeService implements ServiceFactory<SolutionType, Long> {
    @Autowired
    private SolutionTypeRepository solutionTypeRepository;

    @Override
    public SolutionType findById(Long id) {
        return solutionTypeRepository.findById(id).orElse(null);
    }

    @Override
    public List<SolutionType> findAll() {
        return solutionTypeRepository.findAll();
    }

    @Override
    public void save(SolutionType solutionType) {
        solutionTypeRepository.save(solutionType);
    }

    public DataTablesOutput<SolutionType> findAll(DataTablesInput input) {
        return solutionTypeRepository.findAll(input);
    }
}
