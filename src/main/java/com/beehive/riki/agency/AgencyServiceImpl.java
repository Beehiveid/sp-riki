package com.beehive.riki.agency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyServiceImpl implements AgencyService {
    @Autowired
    private AgencyRepository agencyRepository;

    @Override
    public Agency findById(Long id) {
        return agencyRepository.findById(id).orElse(null);
    }

    @Override
    public List<Agency> findAll() {
        return agencyRepository.findAll();
    }

    @Override
    public void save(Agency agency) {
        agencyRepository.save(agency);
    }
}
