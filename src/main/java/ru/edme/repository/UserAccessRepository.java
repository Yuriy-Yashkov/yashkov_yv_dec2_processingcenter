package ru.edme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.model.UserAccess;

import java.util.Optional;

@Repository
public interface UserAccessRepository extends JpaRepository<UserAccess, Long> {

    Optional<UserAccess> findByUserLogin(String userLogin);
}
