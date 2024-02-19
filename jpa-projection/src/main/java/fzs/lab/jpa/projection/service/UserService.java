package fzs.lab.jpa.projection.service;

import fzs.lab.jpa.projection.api.UserController;
import fzs.lab.jpa.projection.domain.Role;
import fzs.lab.jpa.projection.domain.User;
import fzs.lab.jpa.projection.dto.UserDto;
import fzs.lab.jpa.projection.exception.ErrorMessages;
import fzs.lab.jpa.projection.exception.RestExceptionHandler;
import fzs.lab.jpa.projection.repository.RoleRepository;
import fzs.lab.jpa.projection.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<UserDto> getAllUsers() {
        // Ref:1 JPA projection for performance optimization (Only id, username and email needed for each user)
        return userRepository.findByDeletedFalse();
    }

    public User getUserById(Long id) throws RestExceptionHandler.ResourceNotFoundException {
        // Ref:2 Avoided JPA projection for common usage in softDeleteUser(Long id) and updateUser(UpdateUserInput user)
        return userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RestExceptionHandler.ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND.getMessage(id)));
    }

    @Transactional
    public User createUser(UserController.CreateUserInput user) throws RestExceptionHandler.ResourceAlreadyExistsException {
        if (userRepository.existsByEmailAndDeletedFalse(user.getEmail())) {
            throw new RestExceptionHandler.ResourceAlreadyExistsException(ErrorMessages.EMAIL_ALREADY_EXISTS.getMessage(user.getEmail()));
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setAge(user.getAge());
        return userRepository.save(newUser);
    }
    @Transactional
    public User updateUser(UserController.UpdateUserInput user) throws RestExceptionHandler.ResourceNotFoundException {
        // check if user exist or not
        // Ref:2 Avoided JPA projection in getUserById(id) for common usage
        User updateUser = getUserById(user.getId());
        updateUser.setUsername(user.getUsername());
        updateUser.setEmail(user.getEmail());
        updateUser.setAge(user.getAge());
        return userRepository.save(updateUser);
    }

    @Transactional
    public void softDeleteUser(Long id) throws RestExceptionHandler.ResourceNotFoundException {
        // check if user exist or not
        // Ref:2 Avoided JPA projection in getUserById(id) for common usage
        User user = getUserById(id);
        user.setDeleted(true);
        userRepository.save(user);
    }
    public void addRoleToUser(Long userId, Long roleId) throws RestExceptionHandler.ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestExceptionHandler.ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND.getMessage(userId)));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RestExceptionHandler.ResourceNotFoundException(ErrorMessages.ROLE_NOT_FOUND.getMessage(roleId)));

        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }

        user.getRoles().add(role);
        userRepository.save(user);
    }

    public void removeRoleFromUser(Long userId, Long roleId) throws RestExceptionHandler.ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestExceptionHandler.ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND.getMessage(userId)));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RestExceptionHandler.ResourceNotFoundException(ErrorMessages.ROLE_NOT_FOUND.getMessage(roleId)));

        if (user.getRoles() != null) {
            user.getRoles().remove(role);
            userRepository.save(user);
        }
    }
}

