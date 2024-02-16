package fzs.lab.jpa.projection.api;

import fzs.lab.jpa.projection.domain.Role;
import fzs.lab.jpa.projection.dto.RoleDto;
import fzs.lab.jpa.projection.exception.RestException;
import fzs.lab.jpa.projection.response.RestResponse;
import fzs.lab.jpa.projection.response.SuccessMessages;
import fzs.lab.jpa.projection.service.RoleService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    @Getter
    @Setter
    public static class CreateRoleInput {
        @NotBlank(message = "Role name is required")
        private String name;
    }

    @Getter
    @Setter
    public static class UpdateRoleInput {
        @NotNull(message = "Role Id is required")
        private Long id;
        @NotBlank(message = "Role name is required")
        private String name;
    }

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<RestResponse.SuccessResponse> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new RestResponse.SuccessResponse(SuccessMessages.ROLE_LIST.getMessage(roles.size()), roles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse.SuccessResponse> getRoleById(@PathVariable Long id) throws RestException.ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new RestResponse.SuccessResponse(SuccessMessages.ROLE_DETAIL.getMessage(id), roleService.getRoleById(id)));
    }

    @PostMapping
    public ResponseEntity<RestResponse.SuccessResponse> createRole(@RequestBody CreateRoleInput role) throws RestException.ResourceAlreadyExistsException {
        Role newRole = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new RestResponse.SuccessResponse(SuccessMessages.ROLE_CREATED.getMessage(newRole.getName()), newRole));
    }

    @PutMapping
    public ResponseEntity<RestResponse.SuccessResponse> updateRole(@RequestBody UpdateRoleInput role) throws RestException.ResourceNotFoundException {
        Role updatedRole = roleService.updateRole(role);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new RestResponse.SuccessResponse(SuccessMessages.ROLE_UPDATED.getMessage(updatedRole.getName()), updatedRole));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse.SuccessResponse> deleteRole(@PathVariable Long id) throws RestException.ResourceNotFoundException {
        roleService.softDeleteRole(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new RestResponse.SuccessResponse(SuccessMessages.ROLE_DELETED.getMessage(id), null));
    }
}

