package com.dpm.authentication.repository;

import com.dpm.authentication.datamodels.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByEmail(String Email);
    Boolean existsByEmail(String Email);
    void deleteByEmail(String Email);
}
