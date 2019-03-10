package com.beehive.riki.sto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StoRepository extends JpaRepository<Sto, Long>, JpaSpecificationExecutor<Sto> {
}
