package com.pancabudi.technic.complaintType;

import com.pancabudi.technic.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintTypeService {
    @Autowired
    private ComplaintTypeRepository complaintTypeRepository;

    DataTablesOutput<ComplaintType> findAll(DataTablesInput input){
        return complaintTypeRepository.findAll(input);
    }

    private List<ComplaintType> findAll(){
        return  complaintTypeRepository.findAll();
    }

    public List<ComplaintType> findAll(boolean isCommon){
        if(!isCommon){
            return this.findAll();
        }else{
            return complaintTypeRepository.findAll(Specification.where(ComplaintTypeSpecification.isCommon()));
        }

    }

    ComplaintType findById(Long id) {
        return complaintTypeRepository.findById(id).orElse(null);
    }

    public void create(ComplaintType complaintType) {
        complaintTypeRepository.save(complaintType);
    }

    public ComplaintType findByName(String name){
        return complaintTypeRepository.findByName(name);
    }

    public boolean isExist(String type) {
        return complaintTypeRepository.existsByName(type);
    }

    void update(Long id, ComplaintType complaintType) {
        ComplaintType ct = this.findById(id);

        if(ct != null){
            complaintType.setId(ct.getId());
            complaintTypeRepository.save(complaintType);
        }else{
            throw new ResourceNotFoundException("This complaint type");
        }
    }
}
