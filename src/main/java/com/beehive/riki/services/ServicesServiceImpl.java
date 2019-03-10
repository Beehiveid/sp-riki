package com.beehive.riki.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicesServiceImpl implements ServicesService {
    @Autowired
    private ServicesRepository servicesRepository;

    @Override
    public Services findById(Long id) {
        return servicesRepository.findById(id).orElse(null);
    }

    @Override
    public List<Services> findAll() {
        return servicesRepository.findAll();
    }

    @Override
    public void save(Services services) {
        servicesRepository.save(services);
    }
}
