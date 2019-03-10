package com.beehive.riki.system;

import com.beehive.riki.common.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
        return systemEnvironmentRepository.findAll(Specification.where(SESpecifications.byName(SystemConstant.SRO_CONSENT_FLOW_TOKEN))).stream().findFirst().orElse(null);
    }

    @Override
    public SystemEnvironment loadReportingConfig() {
        return systemEnvironmentRepository.findAll(Specification.where(SESpecifications.byName(SystemConstant.REPORTING_TOKEN))).stream().findFirst().orElse(null);
    }

    @Override
    public SystemEnvironment loadUIConfig() {
        return systemEnvironmentRepository.findAll(Specification.where(SESpecifications.byName(SystemConstant.UI_CONFIG_TOKEN))).stream().findFirst().orElse(null);
    }

    @Override
    public SystemEnvironment loadSROPriorityConfig() {
        return systemEnvironmentRepository.findAll(Specification.where(SESpecifications.byName(SystemConstant.SRO_PRIORITY_TOKEN))).stream().findFirst().orElse(null);
    }
}
