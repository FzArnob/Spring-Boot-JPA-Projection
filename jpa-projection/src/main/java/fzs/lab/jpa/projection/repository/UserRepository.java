package fzs.lab.jpa.projection.repository;

import fzs.lab.jpa.projection.domain.User;
import fzs.lab.jpa.projection.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Ref:1 JPA projection for performance optimization
    List<UserDto> findByDeletedFalse();

    // Ref:2 Avoiding JPA projection to get Full entity for later manipulation
    Optional<User> findByIdAndDeletedFalse(Long id);

    boolean existsByEmailAndDeletedFalse(String email);
}