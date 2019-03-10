package com.pancabudi.technic.users;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface UserRepository extends DataTablesRepository<AppUser,Integer> {
    AppUser findByUsername(String username);

    @Nullable
    List<AppUser> findAll();

    List<AppUser> findByRoleId(int id);

    List<AppUser> findByPersonId(long id);

    boolean existsByUsername(String username);
}
