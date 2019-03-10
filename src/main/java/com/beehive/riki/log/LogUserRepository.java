package com.beehive.riki.log;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogUserRepository extends JpaRepository<LogUser, Long> {
}
