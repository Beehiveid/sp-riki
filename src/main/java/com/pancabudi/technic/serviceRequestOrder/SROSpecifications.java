package com.pancabudi.technic.serviceRequestOrder;

import com.pancabudi.technic.machine.Machine;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

class SROSpecifications {
    static Specification<ServiceRequestOrder> monitoring(){
        return (Specification<ServiceRequestOrder>) (root, query, criteriaBuilder) -> {
            Integer[] ints = {9, 8};
            List<Integer> statuses = Arrays.asList(ints);
            query.orderBy(criteriaBuilder.asc(root.get("priority")), criteriaBuilder.asc(root.get("createdTime")));
            return root.get("status").in(statuses);
        };
    }

    static Specification<ServiceRequestOrder> isIdle(){
        return (Specification<ServiceRequestOrder>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), 9);
    }

    static Specification<ServiceRequestOrder> byMachines(List<Machine> machines){
        return (Specification<ServiceRequestOrder>) (root, query, criteriaBuilder) -> root.get("machine").in(machines);
    }

    static Specification<ServiceRequestOrder> byMachine(Machine machine) {
        return (Specification<ServiceRequestOrder>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("machine"), machine);
    }

    static Specification<ServiceRequestOrder> after(Date start){
        return (Specification<ServiceRequestOrder>) (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("createdTime"), start);
    }

    static Specification<ServiceRequestOrder> before(Date end){
        return (Specification<ServiceRequestOrder>) (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("createdTime"), end);
    }

    static Specification<ServiceRequestOrder> byStatuses(List<Integer> statuses) {
        return (Specification<ServiceRequestOrder>) (root, query, criteriaBuilder) -> root.get("status").in(statuses);
    }
}
