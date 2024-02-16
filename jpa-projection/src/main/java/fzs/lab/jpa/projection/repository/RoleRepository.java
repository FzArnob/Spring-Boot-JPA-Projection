package fzs.lab.jpa.projection.repository;

import fzs.lab.jpa.projection.domain.Role;
import fzs.lab.jpa.projection.dto.RoleDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    // Ref:1 JPA projection for performance optimization
    @Query("SELECT r.id as id, r.name as name, COUNT(p) as totalPermissions " +
            "FROM Role r " +
            "LEFT JOIN r.permissions p " +
            "WHERE r.deleted = false " +
            "GROUP BY r.id, r.name")
    List<RoleDto> findByDeletedFalseWithPermissionCount();

    // Ref:2 Avoiding JPA projection to get Full entity for later manipulation
    Optional<Role> findByIdAndDeletedFalse(Long id);

    boolean existsByNameAndDeletedFalse(String name);
}