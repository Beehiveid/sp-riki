package com.beehive.riki.role;

import com.beehive.riki.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public void create(Role role){
        roleRepository.save(role);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    public boolean existByName(String role) {
        return roleRepository.existsByName(role);
    }

    public void update(int id, Role role) {
        Role r = this.findById(id);

        if(r != null){
            role.setId(r.getId());
            roleRepository.save(role);
        }else{
            throw new ResourceNotFoundException("The role");
        }
    }

    private Role findById(int id) {
        return roleRepository.findById(id).orElse(null);
    }
}
