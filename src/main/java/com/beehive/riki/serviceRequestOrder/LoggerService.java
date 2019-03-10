package com.beehive.riki.serviceRequestOrder;

import com.beehive.riki.common.ServiceFactory;

import java.util.List;

public interface LoggerService extends ServiceFactory<Logger, Long> {
    List<Logger> findPickedLog(String id);
}
