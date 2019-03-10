package com.pancabudi.technic.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.pancabudi.technic.common.SystemConstant.*;

@Service
public class SystemEnvironmentServiceImpl implements SystemEnvironmentService {
    @Autowired
    private SystemEnvironmentRepository systemEnvironmentRepository;

    @Override
    public SystemEnvironment findById(Long id) {
        return systemEnvironmentRepository.findById(id).orElse(null);
    }

    @Override
    public List<SystemEnvironment> findAll() {
        return systemEnvironmentRepository.findAll();
    }

    @Override
    public void save(SystemEnvironment systemEnvironment) {
        systemEnvironment.setSystemDate(new Date());
        systemEnvironmentRepository.save(systemEnvironment);
    }

    @Override
    public SystemEnvironment loadSROConfig() {
        return systemEnvironmentRepository.findAll(Specification.where(SESpecifications.byName(SRO_CONSENT_FLOW_TOKEN))).stream().findFirst().orElse(null);
    }

    @Override
    public SystemEnvironment loadReportingConfig() {
        return systemEnvironmentRepository.findAll(Specification.where(SESpecifications.byName(REPORTING_TOKEN))).stream().findFirst().orElse(null);
    }

    @Override
    public SystemEnvironment loadUIConfig() {
        return systemEnvironmentRepository.findAll(Specification.where(SESpecifications.byName(UI_CONFIG_TOKEN))).stream().findFirst().orElse(null);
    }

    @Override
    public SystemEnvironment loadSROPriorityConfig() {
        return systemEnvironmentRepository.findAll(Specification.where(SESpecifications.byName(SRO_PRIORITY_TOKEN))).stream().findFirst().orElse(null);
    }
}
