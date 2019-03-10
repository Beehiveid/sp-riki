package com.beehive.riki.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByClientIdAndSecretKey(String cid, String sky);

    List<Client> findByPid(long pid);
}
