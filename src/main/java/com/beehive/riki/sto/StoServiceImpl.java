package com.beehive.riki.sto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoServiceImpl implements StoService {
    @Autowired
    private StoRepository stoRepository;

    @Override
    public Sto findById(Long id) {
        return stoRepository.findById(id).orElse(null);
    }

    @Override
    public List<Sto> findAll() {
        return stoRepository.findAll();
    }

    @Override
    public void save(Sto sto) {
        stoRepository.save(sto);
    }
}
