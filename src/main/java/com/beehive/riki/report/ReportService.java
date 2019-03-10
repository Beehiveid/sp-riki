package com.beehive.riki.report;


import com.beehive.riki.serviceRequestOrder.SROReportData;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public interface ReportService {
    File pihSroReport(String fileName, HashMap<String, List<SROReportData>> reportData);
    File ceperSroReport(String fileName, HashMap<String, List<SROReportData>> reportData);
}
