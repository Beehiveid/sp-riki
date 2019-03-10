package com.beehive.riki.serviceRequestOrder;

import com.beehive.riki.common.SystemConstant;
import com.beehive.riki.complaintType.ComplaintType;
import com.beehive.riki.complaintType.ComplaintTypeService;
import com.beehive.riki.email.EmailService;
import com.beehive.riki.exception.ResourceNotFoundException;
import com.beehive.riki.location.Location;
import com.beehive.riki.location.LocationService;
import com.beehive.riki.machine.Machine;
import com.beehive.riki.machine.MachineService;
import com.beehive.riki.person.Person;
import com.beehive.riki.person.PersonService;
import com.beehive.riki.report.ReportServiceImpl;
import com.beehive.riki.role.RoleService;
import com.beehive.riki.system.SystemEnvironment;
import com.beehive.riki.system.SystemEnvironmentServiceImpl;
import com.beehive.riki.users.AppUser;
import com.beehive.riki.users.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import static com.beehive.riki.common.SystemConstant.SRO_CONSENT_FLOW_TOKEN;
import static com.beehive.riki.common.SystemConstant.UI_CONFIG_TOKEN;

@Service
public class ServiceRequestOrderService {
    @Autowired
    private ServiceRequestOrderRepository serviceRequestOrderRepository;

    @Autowired
    private ServiceRequestOrderHandlingRepository serviceRequestOrderHandlingRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private MachineService machineService;

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private ReportServiceImpl reportService;

    @Autowired
    private SystemEnvironmentServiceImpl systemEnvironmentService;

    @Autowired
    private LoggerServiceImpl loggerService;

    @Autowired
    private PersonService personService;

    @Autowired
    private LocationService locationService;

    List<ServiceRequestOrder> findAll() {
        return serviceRequestOrderRepository.findAll();
    }

    private void submit(ServiceRequestOrder serviceRequestOrder) {
        serviceRequestOrder.setCreatedTime(new Date());
        serviceRequestOrder.setId(this.SROIdGenerator());
        serviceRequestOrder.setStatus(9);

        if(serviceRequestOrder.getComplaintTypes().isEmpty()){
            throw new RuntimeException("0 Complaint error");
        }

        ServiceRequestOrder sro = serviceRequestOrderRepository.save(serviceRequestOrder);

        Logger logger = new Logger(sro.getCreatedTime(), sro.getId(), sro.getStatus(), sro.getId() + " is submitted by " + sro.getRequester().getName());
        loggerService.save(logger);

        userService.findTechnicians().forEach(
                u->{
                    Context context = new Context();
                    context.setVariable("sro", sro);
                    context.setVariable("technician", u);
                    emailService.creatingSRO(context);
        });
    }

    public void submit(ServiceRequestOrder serviceRequestOrder, boolean isAuto){
        if(isAuto){
            this.submit(serviceRequestOrder);
        }else{
            ComplaintType complaintType = complaintTypeService.findByName(SystemConstant.AUTO_ROUTING_MAINTENANCE);
            boolean find = serviceRequestOrder.getComplaintTypes().parallelStream().anyMatch(c-> c.getId().equals(complaintType.getId()));

            if(!find){
                this.submit(serviceRequestOrder);
            }else{
                throw new RuntimeException("SRO with complaint type '"+complaintType.getName()+"' can't be created manually");
            }
        }
    }

    ServiceRequestOrder findById(String id) {
        return serviceRequestOrderRepository.findById(id).orElse(null);
    }

    private String SROIdGenerator(){
        LocalDate now = LocalDate.now();

        Date min = Date.from(now.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date max = Date.from(now.withDayOfMonth(now.lengthOfMonth()).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        ServiceRequestOrder sro = serviceRequestOrderRepository.findTop1ByCreatedTimeBetweenOrderByCreatedTimeDesc(min, max);
        String year = String.valueOf(now.getYear()).substring(2,4);
        String month = String.format("%2s",String.valueOf(now.getMonthValue())).replace(" ","0");
        String serial;
        if(sro == null){
            serial = "1";
        }else{
            String lastSerial = sro.getId().substring(7,11).replaceFirst("^0+(?!$)", "");
            serial = String.valueOf(Integer.parseInt(lastSerial) + 1);
        }

        String paddedSerial = String.format("%4s",serial).replace(" ","0");
        return "SRO"+year+month+paddedSerial;
    }

    void handlingSubmit(String SROId, ServiceRequestOrderHandling logHandling){
        logHandling.setSystemDate(new Date());
        logHandling.setServiceRequestOrder(new ServiceRequestOrder(SROId));
        logHandling.setId(this.logSROIdGenerator(SROId));

        ServiceRequestOrderHandling log = serviceRequestOrderHandlingRepository.save(logHandling);

        SROConsentFlow cfCode = this.consentFlowCode("close");

        if(cfCode.getFlow() == null){
            this.setStatus(SROId, 1);

            Logger logger = new Logger(new Date(), SROId, log.getServiceRequestOrder().getStatus(), SROId + " is handled and auto close by " + logHandling.getHandler().getName() + " trough " + log.getId());
            loggerService.save(logger);
        }else{
            ServiceRequestOrder sro = log.getServiceRequestOrder();

            if(sro.getStatus() != 8)
                throw new RuntimeException("this SRO can't be handled because the status is not 8. SRO Status is "+sro.getStatus());

            this.setStatus(SROId, 7);

            Logger logger = new Logger(new Date(), SROId, log.getServiceRequestOrder().getStatus(), SROId + " is handled by " + logHandling.getHandler().getName() + " trough " + log.getId());
            loggerService.save(logger);
        }

        ServiceRequestOrder sro = this.findById(SROId);
        sro.getMachine().setLastRepair(new Date());

        ComplaintType complaintType = complaintTypeService.findByName(SystemConstant.AUTO_ROUTING_MAINTENANCE);

        if(sro.getComplaintTypes().contains(complaintType)){
            if(sro.getMachine().getMaintenanceDateReference() == 1){
                sro.getMachine().setLastRoutineMaintenance(sro.getMachine().getLastRepair());
            }
        }

        machineService.update(sro.getMachine().getId(),sro.getMachine());

        Context context = new Context();
        context.setVariable("sro", sro);
        context.setVariable("log",log);
        emailService.handlingSRO(context);
    }

    private String logSROIdGenerator(String sroId) {
        ServiceRequestOrderHandling logHandling = serviceRequestOrderHandlingRepository.findTop1ByServiceRequestOrderIdOrderBySystemDateDesc(sroId);
        String serial;
        if(logHandling == null){
            serial = "1";
        }else{
            String lastSerial = logHandling.getId().substring(12,16).replaceFirst("^0+(?!$)", "");
            serial = String.valueOf(Integer.parseInt(lastSerial) + 1);
        }
        String paddedSerial = String.format("%4s",serial).replace(" ","0");
        return sroId+"-"+paddedSerial;
    }

    List<ServiceRequestOrderHandling> handlingList(String id) {
        return serviceRequestOrderHandlingRepository.findByServiceRequestOrderId(id);
    }

    DataTablesOutput<ServiceRequestOrder> findAll(DataTablesInput input, boolean onlyIdle){
        List<Location> relatedLoc = personService.getLocation();
        List<Machine> relatedMachines = machineService.findByLocation(relatedLoc);

        if(onlyIdle){
            return serviceRequestOrderRepository.findAll(input, Specification.where(SROSpecifications.monitoring()).and(SROSpecifications.byMachines(relatedMachines)));
        }else{
            return serviceRequestOrderRepository.findAll(input, Specification.where(SROSpecifications.byMachines(relatedMachines)));
        }
    }

    public void update(String id, ServiceRequestOrder serviceRequestOrder) {
        ServiceRequestOrder sro = this.findById(id);

        if(sro != null){
            serviceRequestOrder.setId(sro.getId());
            serviceRequestOrderRepository.save(serviceRequestOrder);
        }else{
            throw new ResourceNotFoundException("SRO");
        }
    }

    private void setStatus(String id, int status) {
        ServiceRequestOrder sro = this.findById(id);

        if(sro != null){
            sro.setStatus(status);

            if(status == 1){
                sro.setDoneTime(new Date());
            }

            serviceRequestOrderRepository.save(sro);
        }else{
            throw new ResourceNotFoundException("SRO");
        }
    }

    public List<ServiceRequestOrder> findByMachine(Machine m){
        return serviceRequestOrderRepository.findAll(Specification.where(SROSpecifications.byMachine(m)));
    }

    public List<ServiceRequestOrderHandling> findHandlingBySRO(ServiceRequestOrder sro) {
        return serviceRequestOrderHandlingRepository.findByServiceRequestOrderId(sro.getId());
    }

    public List<ServiceRequestOrder> findOutstanding() {
        return serviceRequestOrderRepository.findAll(Specification.where(SROSpecifications.isIdle()));
    }

    public List<ServiceRequestOrder> findOutstandingByMachine(Machine m) {
        return serviceRequestOrderRepository.findAll(Specification.where(SROSpecifications.isIdle()).and(SROSpecifications.byMachine(m)));
    }

    File report(String start, String end, int type) throws ParseException {
        SystemEnvironment rc = systemEnvironmentService.loadReportingConfig();
        String fileName = start+"_"+end+"_"+type+".xlsx";

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate = formatter.parse(start);
        Date endDate = formatter.parse(end);

        List<ServiceRequestOrder> sro = this.findBetweenDates(startDate, endDate);

        HashMap<String,List<SROReportData>> sroReports = this.groupingSRO(sro, type);

        if(rc.getValue().equals("pih")){
            return reportService.pihSroReport(fileName, sroReports);
        }else{
            return reportService.ceperSroReport(fileName, sroReports);
        }
    }

    private HashMap<String, List<SROReportData>> groupingSRO(List<ServiceRequestOrder> sro, int type) {
        HashMap<String, List<SROReportData>> temp = new HashMap<>();
        SystemEnvironment rc = systemEnvironmentService.loadReportingConfig();

        sro.forEach(
                s->{
                    String key;
                    if(type == 0){
                        key = "Sheet 1";
                    }else{
                        key = s.getMachine().getName()+" "+s.getMachine().getLocation().getName();
                    }

                    List<ServiceRequestOrderHandling> sroLog = this.findHandlingBySRO(s);

                    List<SROReportData> sroReportDataList = new ArrayList<>();

                    if(sroLog.size() < 1){
                        SROReportData data = new SROReportData();
                        String[] complaints = new String[s.getComplaintTypes().size()];
                        int i = 0;
                        for (ComplaintType complaintType : s.getComplaintTypes()) {
                            complaints[i++] = complaintType.getName();
                        }

                        data.setComplaint(String.join(", ", complaints));
                        data.setCreatedDate(s.getCreatedTime());
                        data.setRequester(s.getRequester().getName());
                        data.setMachine(s.getMachine().getName());

                        if(!rc.getValue().equals("pih")){
                            data.setFirstResponseDate(this.getFirstResponseDate(s));
                        }

                        sroReportDataList.add(data);
                    }else{
                        sroLog.forEach(
                                log->{
                                    SROReportData data = new SROReportData();
                                    ServiceRequestOrder header = log.getServiceRequestOrder();

                                    String[] complaints = new String[header.getComplaintTypes().size()];
                                    int i = 0;
                                    for (ComplaintType complaintType : header.getComplaintTypes()) {
                                        complaints[i++] = complaintType.getName();
                                    }

                                    data.setComplaint(String.join(", ", complaints));
                                    data.setCreatedDate(header.getCreatedTime());
                                    data.setDoneTime(log.getSystemDate());
                                    data.setHandler(log.getHandler().getName());
                                    data.setHandling(log.getHandling());
                                    data.setPart(log.getPart());
                                    data.setRequester(header.getRequester().getName());
                                    data.setResult(log.getResult());
                                    data.setMachine(header.getMachine().getName());

                                    if(!rc.getValue().equals("pih")){
                                        data.setFirstResponseDate(this.getFirstResponseDate(s));
                                    }

                                    sroReportDataList.add(data);
                                }
                        );
                    }
                    if(!temp.containsKey(key)){
                        temp.put(key, new ArrayList<>());
                    }

                    temp.get(key).addAll(sroReportDataList);
                }
        );

        return temp;
    }

    private Date getFirstResponseDate(ServiceRequestOrder s) {
        List<Logger> pickedLog = loggerService.findPickedLog(s.getId());
        Logger logger = pickedLog.stream().findFirst().orElse(null);

        if(logger != null){
            return logger.getSystemDate();
        }

        return null;
    }

    private List<ServiceRequestOrder> findBetweenDates(Date startDate, Date endDate) {
        return serviceRequestOrderRepository.findAll(Specification.where(SROSpecifications.after(startDate)).and(SROSpecifications.before(endDate)));
    }

    boolean consentFlow(String code, Person p){
        AppUser user = userService.findByPerson(p);
        SROConsentFlow cfCode = this.consentFlowCode(code);

        return cfCode.getFlow().equals(code) && (cfCode.getRoles().contains(user.getRole().getId()) || user.getRole().getId() == 0);
    }

    private SROConsentFlow consentFlowCode(String code) {
        SystemEnvironment sroConfig = systemEnvironmentService.loadSROConfig();
        SROConsentFlow[] sroConsentFlows;

        try {
            sroConsentFlows = new ObjectMapper().readValue(sroConfig.getValue(), SROConsentFlow[].class);
        } catch (IOException e) {
            throw new RuntimeException("Some error occured on SRO Consent Flow");
        }

        SROConsentFlow exactCF = new SROConsentFlow();

        for (SROConsentFlow consentFlow : sroConsentFlows) {
            if(consentFlow.getFlow().equals(code)){
                exactCF = consentFlow;
                break;
            }
        }

        return exactCF;
    }

    void pick(String id, Person picker) {
        ServiceRequestOrder sro = this.findById(id);

        if(sro == null)
            throw new ResourceNotFoundException("SRO");

        if(sro.getStatus() != 9)
            throw new RuntimeException("this SRO can't be picked because the status is not 9. SRO Status is "+sro.getStatus());

        this.setStatus(id, 8);

        Logger logger = new Logger(new Date(), sro.getId(), sro.getStatus(), sro.getId() + " is picked by " + picker.getName());
        loggerService.save(logger);
    }

    void accept(String id, Person acceptor){
        ServiceRequestOrder sro = this.findById(id);

        if(sro == null)
            throw new ResourceNotFoundException("SRO");

        if(sro.getStatus() != 7)
            throw new RuntimeException("this SRO can't be done because the status is not 7. SRO Status is "+sro.getStatus());

        this.setStatus(id, 1);

        Logger logger = new Logger(new Date(), sro.getId(), sro.getStatus(), sro.getId() + " is accepted by " + acceptor.getName());
        loggerService.save(logger);
    }

    public void postConsent(String code) {
        SystemEnvironment sroFlowConfig = systemEnvironmentService.loadSROConfig();

        if(sroFlowConfig == null){
            sroFlowConfig = new SystemEnvironment();
            sroFlowConfig.setToken(SRO_CONSENT_FLOW_TOKEN);
        }

        List<SROConsentFlow> sroConsentFlows = new ArrayList<>();

        sroConsentFlows.add(this.setConsentFlow("create",1));
        sroConsentFlows.add(this.setConsentFlow("cancel",1));

        if(code.equals("pih")){
            sroConsentFlows.add(this.setConsentFlow("handle",2));
        }else{
            sroConsentFlows.add(this.setConsentFlow("pick",2));
            sroConsentFlows.add(this.setConsentFlow("handle",2));
            sroConsentFlows.add(this.setConsentFlow("close",1));
        }

        String cf = null;

        try {
            cf = new ObjectMapper().writeValueAsString(sroConsentFlows);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        sroFlowConfig.setValue(cf);
        sroFlowConfig.setSystemDate(new Date());
        systemEnvironmentService.save(sroFlowConfig);

        SystemEnvironment uiConfig = systemEnvironmentService.loadUIConfig();

        if(uiConfig == null){
            uiConfig = new SystemEnvironment();
            uiConfig.setToken(UI_CONFIG_TOKEN);
        }

        uiConfig.setSystemDate(new Date());
        uiConfig.setValue(code);
        systemEnvironmentService.save(uiConfig);
    }

    private SROConsentFlow setConsentFlow(String code, int... ints) {
        SROConsentFlow cf = new SROConsentFlow();
        cf.setFlow(code);

        for (int i : ints) {
            cf.getRoles().add(i);
        }

        return cf;
    }

    void cancel(String id, Person person) {
        ServiceRequestOrder sro = this.findById(id);

        if(sro == null)
            throw new ResourceNotFoundException("SRO");

        this.setStatus(id, 0);

        Logger logger = new Logger(new Date(), sro.getId(), sro.getStatus(), sro.getId() + " is cancelled by " + person.getName());
        loggerService.save(logger);
    }

    HashMap<String, List<Object>> getRelevantMachine() {
        List<Location> relatedLoc = personService.getLocation();

        HashMap<String,List<Object>> relevantData = new HashMap<>();
        relevantData.put("locations", new ArrayList<>());
        relevantData.put("machineTypes", new ArrayList<>());
        relevantData.put("machines", new ArrayList<>());

        relevantData.get("locations").addAll(relatedLoc);

        List<Machine> relatedMachines = machineService.findByLocation(relatedLoc);

        relatedMachines.forEach(
                m->{
                    if(!relevantData.get("machineTypes").contains(m.getMachineType()))
                        relevantData.get("machineTypes").add(m.getMachineType());
                }
        );

        relevantData.get("machines").addAll(relatedMachines);

        return relevantData;
    }

    public DataTablesOutput<ServiceRequestOrder> findAll(DataTablesInput input, long location) {
        Location loc = locationService.findById(location);

        if(loc == null)
            throw new ResourceNotFoundException("Location");

        List<Location> locations = new ArrayList<>();
        locations.add(loc);

        List<Machine> relatedMachines = machineService.findByLocation(locations);
        if(relatedMachines.size() < 1){
            return new DataTablesOutput<>();
        }

        return serviceRequestOrderRepository.findAll(input, Specification.where(SROSpecifications.isIdle()).and(SROSpecifications.byMachines(relatedMachines)));
    }

    public DataTablesOutput<ServiceRequestOrder> findAll(DataTablesInput input, Integer... statuses) {
        List<Integer> s = Arrays.asList(statuses);
        return serviceRequestOrderRepository.findAll(input, Specification.where(SROSpecifications.byStatuses(s)));
    }
}
