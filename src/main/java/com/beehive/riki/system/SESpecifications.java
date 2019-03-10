package com.beehive.riki.system;

import org.springframework.data.jpa.domain.Specification;

class SESpecifications {
    static Specification<SystemEnvironment> byName(String name){
        return (Specification<SystemEnvironment>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("token"), name);
    }
}
