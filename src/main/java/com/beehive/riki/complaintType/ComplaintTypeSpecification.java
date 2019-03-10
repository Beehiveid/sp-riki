package com.beehive.riki.complaintType;

import com.beehive.riki.common.SystemConstant;
import org.springframework.data.jpa.domain.Specification;

class ComplaintTypeSpecification {
    static Specification<ComplaintType> isCommon(){
        return (Specification<ComplaintType>) (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("name"), SystemConstant.AUTO_ROUTING_MAINTENANCE);
    }
}
