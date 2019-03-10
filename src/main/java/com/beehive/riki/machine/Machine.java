package com.beehive.riki.machine;

import com.beehive.riki.location.Location;
import com.beehive.riki.machineType.MachineType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="machine_type_id")
    private MachineType machineType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    private Location location;

    private String name;

    @NotNull
    private Date assembleDate;
    private Date lastRepair;
    private Date lastRoutineMaintenance;
    private int routineCycle;
    private int dueDateWarning;
    private int maintenanceDateReference;

    Machine(Machine m) {
        this.id = m.getId();
        this.machineType = m.getMachineType();
        this.location = m.getLocation();
        this.name = m.getName();
        this.assembleDate = m.getAssembleDate();
        this.lastRepair = m.getLastRepair();
        this.lastRoutineMaintenance = m.getLastRoutineMaintenance();
        this.routineCycle = m.getRoutineCycle();
        this.dueDateWarning = m.getDueDateWarning();
        this.maintenanceDateReference = m.getMaintenanceDateReference();
    }

    public int getDueDateWarning() {
        return dueDateWarning;
    }

    public void setDueDateWarning(int dueDateWarning) {
        this.dueDateWarning = dueDateWarning;
    }

    public int getMaintenanceDateReference() {
        return maintenanceDateReference;
    }

    public void setMaintenanceDateReference(int maintenanceDateReference) {
        this.maintenanceDateReference = maintenanceDateReference;
    }

    public Date getLastRoutineMaintenance() {
        return lastRoutineMaintenance;
    }

    public void setLastRoutineMaintenance(Date lastRoutineMaintenance) {
        this.lastRoutineMaintenance = lastRoutineMaintenance;
    }

    public int getRoutineCycle() {
        return routineCycle;
    }

    public void setRoutineCycle(int routineCycle) {
        this.routineCycle = routineCycle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MachineType getMachineType() {
        return machineType;
    }

    public void setMachineType(MachineType machineType) {
        this.machineType = machineType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getAssembleDate() {
        return assembleDate;
    }

    public void setAssembleDate(Date assembleDate) {
        this.assembleDate = assembleDate;
    }

    public Date getLastRepair() {
        return lastRepair;
    }

    public void setLastRepair(Date lastRepair) {
        this.lastRepair = lastRepair;
    }

    public Machine() {
    }
}
