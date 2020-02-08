package ua.foodtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.foodtracker.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);
}
