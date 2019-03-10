package com.pancabudi.technic.person;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByName(String name);

    boolean existsByName(String name);

    Person findByEmail(String email);
}
