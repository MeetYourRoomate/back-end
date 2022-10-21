package com.meetyourroommate.app.iam.infrastructure.persistance.jpa;

import com.meetyourroommate.app.iam.domain.aggregates.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {
}
