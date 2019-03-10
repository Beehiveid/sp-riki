package com.beehive.riki.machineType;

import com.beehive.riki.complaintType.ComplaintType;
import com.beehive.riki.solutionType.SolutionType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class MachineType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max=60)
    @Column(unique = true, updatable = false)
    private String name;
    private String note;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rel_machine_complaint",
            joinColumns = @JoinColumn(name = "machine_type_id"),
            inverseJoinColumns = @JoinColumn(name = "complaint_type_id"))
    private Set<ComplaintType> complaintTypes = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rel_machine_solution",
            joinColumns = @JoinColumn(name = "machine_type_id"),
            inverseJoinColumns = @JoinColumn(name = "solution_type_id"))
    private Set<SolutionType> solutionTypes = new HashSet<>();

    public Set<SolutionType> getSolutionTypes() {
        return solutionTypes;
    }

    public void setSolutionTypes(Set<SolutionType> solutionTypes) {
        this.solutionTypes = solutionTypes;
    }

    public Set<ComplaintType> getComplaintTypes() {
        return complaintTypes;
    }

    public void setComplaintTypes(Set<ComplaintType> complaintTypes) {
        this.complaintTypes = complaintTypes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
