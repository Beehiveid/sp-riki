package com.beehive.riki.system;

import com.beehive.riki.common.ServiceFactory;

public interface SystemEnvironmentService extends ServiceFactory<SystemEnvironment, Long> {
    SystemEnvironment loadSROConfig();
    SystemEnvironment loadReportingConfig();
    SystemEnvironment loadUIConfig();

    SystemEnvironment loadSROPriorityConfig();
}
