package com.pancabudi.technic.machine;

import com.pancabudi.technic.common.SystemConstant;
import com.pancabudi.technic.complaintType.ComplaintType;
import com.pancabudi.technic.complaintType.ComplaintTypeService;
import com.pancabudi.technic.exception.ResourceNotFoundException;
import com.pancabudi.technic.location.Location;
import com.pancabudi.technic.location.LocationService;
import com.pancabudi.technic.machineType.MachineType;
import com.pancabudi.technic.machineType.MachineTypeService;
import com.pancabudi.technic.serviceRequestOrder.ServiceRequestOrderHandling;
import com.pancabudi.technic.serviceRequestOrder.ServiceRequestOrder;
import com.pancabudi.technic.serviceRequestOrder.ServiceRequestOrderService;
import com.pancabudi.technic.users.AppUser;
import com.pancabudi.technic.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class MachineService {
    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private ServiceRequestOrderService serviceRequestOrderService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private MachineTypeService machineTypeService;

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private UserService userService;

    List<Machine> findAll() {
        return machineRepository.findAll();
    }

    Machine findById(Long id) {
        return machineRepository.findById(id).orElse(null);
    }

    void create(Machine machine) {
        machineRepository.save(machine);
    }

    DataTablesOutput<Machine> findAll(DataTablesInput input){
        return machineRepository.findAll(input);
    }

    public void routineMaintenance(){
        List<Machine> machines = this.findAll();
        LocalDate today = LocalDate.now();

        List<Machine> maintainMachines = new ArrayList<>();

        machines.forEach(
                m->{
                    Date reference;
                    if(m.getLastRoutineMaintenance() == null){
                        reference = m.getAssembleDate();
                    }else{
                        reference = m.getLastRoutineMaintenance();
                    }

                    LocalDate lrm = reference.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    long diff = ChronoUnit.DAYS.between(lrm,today);

                    if(diff == m.getRoutineCycle() - m.getDueDateWarning()){
                        maintainMachines.add(m);
                    }
                }
        );

        ComplaintType complaintType = complaintTypeService.findByName(SystemConstant.AUTO_ROUTING_MAINTENANCE);
        AppUser appUser = userService.findAdministrator().get(0);

        Set<ComplaintType> complaintTypes = new HashSet<>();
        complaintTypes.add(complaintType);

        if(complaintType != null && appUser != null){
            maintainMachines.forEach(
                    mm->{
                        ServiceRequestOrder sro = new ServiceRequestOrder();
                        sro.setMachine(mm);
                        sro.setNote(complaintType.getName());
                        sro.setPriority(5);
                        sro.setRequester(appUser.getPerson());
                        sro.setComplaintTypes(complaintTypes);

                        serviceRequestOrderService.submit(sro, true);

                        if(mm.getMaintenanceDateReference() == 0){
                            Date reference;
                            if(mm.getLastRoutineMaintenance() == null){
                                reference = mm.getAssembleDate();
                            }else{
                                reference = mm.getLastRoutineMaintenance();
                            }

                            LocalDate newMaintenanceDate = reference.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(mm.getRoutineCycle());
                            mm.setLastRoutineMaintenance(Date.from(newMaintenanceDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                            this.update(mm.getId(), mm);
                        }
                    }
            );
        }
    }

    public void update(Long id, Machine machine) {
        Machine mc = this.findById(id);

        if(mc != null){
            machine.setId(mc.getId());
            machineRepository.save(machine);
        }else{
            throw new ResourceNotFoundException("This machine");
        }
    }

    List<MachineHistory> history(long id) {
        Machine machine = this.findById(id);

        if(machine != null){
            List<MachineHistory> machineHistories = new ArrayList<>();
            List<ServiceRequestOrderHandling> logs = new ArrayList<>();
            List<ServiceRequestOrder> serviceRequestOrders = serviceRequestOrderService.findByMachine(machine);

            serviceRequestOrders.forEach(
                    sro -> logs.addAll(serviceRequestOrderService.findHandlingBySRO(sro))
            );

            logs.forEach(
                    log -> machineHistories.add(new MachineHistory(log))
            );

            return machineHistories;
        }
        else{
            throw new ResourceNotFoundException("Machine");
        }
    }

    List<MachineInformation> getMachinesInformation() {
        List<ServiceRequestOrder> outstandingSRO = serviceRequestOrderService.findOutstanding();
        List<Machine> machines = this.findAll();
        List<MachineInformation> machineInformationList = new ArrayList<>();

        machines.forEach(
                m->{
                    int count = (int) outstandingSRO.parallelStream().filter(os -> os.getMachine().equals(m)).count();
                    machineInformationList.add(new MachineInformation(m, count));
                }
        );

        return machineInformationList;
    }

    MachineInformation getMachinesInformation(Long id) {
        Machine machine = this.findById(id);

        if(machine != null){
            int count = serviceRequestOrderService.findOutstandingByMachine(machine).size();
            return new MachineInformation(machine, count);
        }else{
            throw new ResourceNotFoundException("Machine");
        }
    }

    public List<Machine> findByLocation(List<Location> relatedLoc) {
        return machineRepository.findAll(Specification.where(MachineSpecifications.byLocation(relatedLoc)));
    }

    @Deprecated
    public void bulk() {
        List<Machine> machines = new ArrayList<>();
        Location location = locationService.findById(1);

        // todo TIUP PE
        MachineType tiupPE = machineTypeService.findById(1L);
        int num = 59;
        for (int i = 1; i <= num; i++) {
            String baseName = "T-PE-";
            Machine machine = new Machine();
            String serial = String.valueOf(i);
            String paddedSerial = String.format("%3s",serial).replace(" ","0");
            String newName = baseName + paddedSerial;
            System.out.println(newName);

            machine.setName(newName);
            machine.setLocation(location);
            machine.setMachineType(tiupPE);
            machines.add(machine);
        }
        // todo POTONG PE
        MachineType potongPE = machineTypeService.findById(2L);
        num = 40;
        for (int i = 1; i <= num; i++) {
            String baseName = "P-PE-";
            Machine machine = new Machine();
            String serial = String.valueOf(i);
            String paddedSerial = String.format("%3s",serial).replace(" ","0");
            String newName = baseName + paddedSerial;
            System.out.println(newName);

            machine.setName(newName);
            machine.setLocation(location);
            machine.setMachineType(potongPE);
            machines.add(machine);
        }

        //todo TIUP HD R
        MachineType tiupHdR = machineTypeService.findById(3L);
        num = 13;
        for (int i = 1; i <= num; i++) {
            String baseName = "T-HDR-";
            Machine machine = new Machine();
            String serial = String.valueOf(i);
            String paddedSerial = String.format("%3s",serial).replace(" ","0");
            String newName = baseName + paddedSerial;
            System.out.println(newName);

            machine.setName(newName);
            machine.setLocation(location);
            machine.setMachineType(tiupHdR);
            machines.add(machine);
        }

        //todo TIUP HD PK
        MachineType tiupHdPk = machineTypeService.findById(4L);
        Integer[] arr = new Integer[]{7,8,9,11};
        for (int i = 0; i < arr.length; i++) {
            String baseName = "T-HDK-";
            Machine machine = new Machine();
            String serial = String.valueOf(arr[i]);
            String paddedSerial = String.format("%3s",serial).replace(" ","0");
            String newName = baseName + paddedSerial;
            System.out.println(newName);

            machine.setName(newName);
            machine.setLocation(location);
            machine.setMachineType(tiupHdPk);
            machines.add(machine);
        }

        //todo TIUP PE PK
        MachineType tiupPePk = machineTypeService.findById(5L);
        num = 6;
        for (int i = 1; i <= num; i++) {
            String baseName = "T-PEK-";
            Machine machine = new Machine();
            String serial = String.valueOf(i);
            String paddedSerial = String.format("%3s",serial).replace(" ","0");
            String newName = baseName + paddedSerial;
            System.out.println(newName);

            machine.setName(newName);
            machine.setLocation(location);
            machine.setMachineType(tiupPePk);
            machines.add(machine);
        }

        //todo POTONG HD R
        MachineType potongHdR = machineTypeService.findById(6L);
        num = 19;
        for (int i = 1; i <= num; i++) {
            String baseName = "P-HDR-";
            Machine machine = new Machine();
            String serial = String.valueOf(i);
            String paddedSerial = String.format("%3s",serial).replace(" ","0");
            String newName = baseName + paddedSerial;
            System.out.println(newName);

            machine.setName(newName);
            machine.setLocation(location);
            machine.setMachineType(potongHdR);
            machines.add(machine);
        }

        machines.forEach(
                m->{
                    m.setAssembleDate(new Date());
                }
        );

        machineRepository.saveAll(machines);
    }
}
