package com.beehive.riki.technician;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechnicianServiceImpl implements TechnicianService {
    @Autowired
    private TechnicianRepository technicianRepository;

    @Override
    public Technician findById(Long id) {
        return technicianRepository.findById(id).orElse(null);
    }

    @Override
    public List<Technician> findAll() {
        return technicianRepository.findAll();
    }

    @Override
    public void save(Technician technician) {
        technicianRepository.save(technician);
    }
}
