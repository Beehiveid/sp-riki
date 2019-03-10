package com.beehive.riki.serviceRequestOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
@Service
public class LoggerServiceImpl implements LoggerService {
    @Autowired
    private LoggerRepository loggerRepository;

    @Override
    public Logger findById(Long id) {
        return loggerRepository.findById(id).orElse(null);
    }

    @Override
    public List<Logger> findAll() {
        return loggerRepository.findAll();
    }

    @Override
    public void save(Logger logger) {
        loggerRepository.save(logger);
    }

    @Override
    public List<Logger> findPickedLog(String id) {
        return loggerRepository.findAll(Specification.where(LoggerSpecifications.bySRO(id)).and(LoggerSpecifications.isPicked()));
    }
}
