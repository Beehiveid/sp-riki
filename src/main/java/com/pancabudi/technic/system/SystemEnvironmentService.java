package com.pancabudi.technic.system;

import com.pancabudi.technic.common.ServiceFactory;

public interface SystemEnvironmentService extends ServiceFactory<SystemEnvironment, Long> {
    SystemEnvironment loadSROConfig();
    SystemEnvironment loadReportingConfig();
    SystemEnvironment loadUIConfig();

    SystemEnvironment loadSROPriorityConfig();
}
