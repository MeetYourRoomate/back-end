package com.meetyourroommate.app.iam.infrastructure.persistance.jpa;

import java.util.Optional;

import com.meetyourroommate.app.iam.domain.entities.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meetyourroommate.app.iam.domain.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);
  Optional<Role> findByName(Roles roleName);
}

