package fzs.lab.jpa.projection.service;

import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleDto> getAllRoles() {
        return roleRepository.findByDeletedFalse();
    }

    public Role getRoleById(Long id) throws RestException.ResourceNotFoundException {
        return roleRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RestException.ResourceNotFoundException(ErrorMessages.ROLE_NOT_FOUND.getMessage(id)));
    }

    @Transactional
    public Role createRole(RoleController.CreateRoleInput role) {
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

