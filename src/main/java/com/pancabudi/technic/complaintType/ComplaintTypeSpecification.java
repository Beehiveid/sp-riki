package com.pancabudi.technic.complaintType;

import com.pancabudi.technic.common.SystemConstant;
import org.springframework.data.jpa.domain.Specification;

class ComplaintTypeSpecification {
    static Specification<ComplaintType> isCommon(){
        return (Specification<ComplaintType>) (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("name"), SystemConstant.AUTO_ROUTING_MAINTENANCE);
    }
}
