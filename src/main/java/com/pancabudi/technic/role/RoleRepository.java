package com.pancabudi.technic.role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    boolean existsByName(String name);

    Role findByName(String name);
}
