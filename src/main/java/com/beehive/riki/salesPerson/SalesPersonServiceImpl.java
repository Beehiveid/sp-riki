package com.beehive.riki.salesPerson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesPersonServiceImpl implements SalesPersonService {
    @Autowired
    private SalesPersonRepository salesPersonRepository;

    @Override
    public SalesPerson findById(Long id) {
        return salesPersonRepository.findById(id).orElse(null);
    }

    @Override
    public List<SalesPerson> findAll() {
        return salesPersonRepository.findAll();
    }

    @Override
    public void save(SalesPerson salesPerson) {
        salesPersonRepository.save(salesPerson);
    }
}
