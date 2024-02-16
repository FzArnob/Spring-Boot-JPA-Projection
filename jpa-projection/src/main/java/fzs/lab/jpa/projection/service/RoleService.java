package fzs.lab.jpa.projection.service;

import fzs.lab.jpa.projection.api.RoleController;
import fzs.lab.jpa.projection.domain.Role;
import fzs.lab.jpa.projection.dto.RoleDto;
import fzs.lab.jpa.projection.exception.ErrorMessages;
import fzs.lab.jpa.projection.exception.RestException;
import fzs.lab.jpa.projection.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleDto> getAllRoles() {
        return roleRepository.findByDeletedFalseWithPermissionCount();
    }

    public Role getRoleById(Long id) throws RestException.ResourceNotFoundException {
        return roleRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RestException.ResourceNotFoundException(ErrorMessages.ROLE_NOT_FOUND.getMessage(id)));
    }

    @Transactional
    public Role createRole(RoleController.CreateRoleInput role) throws RestException.ResourceAlreadyExistsException {
        if (roleRepository.existsByNameAndDeletedFalse(role.getName())) {
            throw new RestException.ResourceAlreadyExistsException(ErrorMessages.ROLE_ALREADY_EXISTS.getMessage(role.getName()));
        }
        Role newRole = new Role();
        newRole.setName(role.getName());
        return roleRepository.save(newRole);
    }

    @Transactional
    public Role updateRole(RoleController.UpdateRoleInput role) throws RestException.ResourceNotFoundException {
        Role updateRole = getRoleById(role.getId());
        updateRole.setName(role.getName());
        return roleRepository.save(updateRole);
    }

    @Transactional
    public void softDeleteRole(Long id) throws RestException.ResourceNotFoundException {
        Role role = getRoleById(id);
        role.setDeleted(true);
        roleRepository.save(role);
    }
}

