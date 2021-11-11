package io.javaminds.application.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import io.javaminds.application.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	Role findByName(String name);
}
