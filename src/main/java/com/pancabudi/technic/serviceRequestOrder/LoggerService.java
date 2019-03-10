package com.pancabudi.technic.serviceRequestOrder;

import com.pancabudi.technic.common.ServiceFactory;

import java.util.List;

public interface LoggerService extends ServiceFactory<Logger, Long> {
    List<Logger> findPickedLog(String id);
}
