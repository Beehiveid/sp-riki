package com.beehive.riki.machine;

import com.beehive.riki.location.Location;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class MachineSpecifications {
    static Specification<Machine> byLocation(List<Location> locations){
        return (Specification<Machine>) (root, query, criteriaBuilder) -> root.get("location").in(locations);
    }
}
