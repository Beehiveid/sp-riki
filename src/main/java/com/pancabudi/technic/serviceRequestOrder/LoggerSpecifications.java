package com.pancabudi.technic.serviceRequestOrder;

import org.springframework.data.jpa.domain.Specification;

class LoggerSpecifications {
    static Specification<Logger> bySRO(String id){
        return (Specification<Logger>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("sroId"), id);
    }

    static Specification<Logger> isPicked(){
        return (Specification<Logger>) (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("systemDate")));
            return criteriaBuilder.equal(root.get("sroStatus"), 8);
        };
    }
}
