package com.pancabudi.technic.machineType;

import com.pancabudi.technic.common.SystemConstant;
import com.pancabudi.technic.complaintType.ComplaintType;
import com.pancabudi.technic.complaintType.ComplaintTypeService;
import com.pancabudi.technic.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineTypeService {
    @Autowired
    private MachineTypeRepository machineTypeRepository;

    @Autowired
    private ComplaintTypeService complaintTypeService;

    List<MachineType> findAll() {
        return machineTypeRepository.findAll();
    }

    void create(MachineType machineType) {
        ComplaintType complaintType = complaintTypeService.findByName(SystemConstant.AUTO_ROUTING_MAINTENANCE);
        boolean find = machineType.getComplaintTypes().parallelStream().anyMatch(c->c.getId().equals(complaintType.getId()));

        if(!find){
            this.save(machineType);
        }else{
            throw new RuntimeException(complaintType.getName()+" doesn't need to set up to machine type manually");
        }
    }

    public MachineType findById(Long id) {
        return machineTypeRepository.findById(id).orElse(null);
    }

    DataTablesOutput<MachineType> findAll(DataTablesInput input){
        return machineTypeRepository.findAll(input);
    }

    public void update(Long id, MachineType machineType) {
        MachineType mt = this.findById(id);

        if(mt != null){
            machineType.setId(mt.getId());
            this.save(machineType);
        }else{
            throw new ResourceNotFoundException("Machine");
        }
    }

    void copyConfiguration(Long from, Long to) {
        MachineType sourceMachine = this.findById(from);
        MachineType targetMachine = this.findById(to);

        if(sourceMachine == null || targetMachine == null)
            throw new RuntimeException("some of this machine type is not exist");

        sourceMachine.getSolutionTypes().forEach(
                s-> targetMachine.getSolutionTypes().add(s)
        );

        sourceMachine.getComplaintTypes().forEach(
                c-> targetMachine.getComplaintTypes().add(c)
        );

        this.save(targetMachine);
    }

    private void save(MachineType machineType){
        machineTypeRepository.save(machineType);
    }
}
