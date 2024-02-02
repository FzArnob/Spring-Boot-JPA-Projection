package fzs.lab.jpa.projection.service;

import fzs.lab.jpa.projection.domain.User;
import fzs.lab.jpa.projection.dto.UserDto;
import fzs.lab.jpa.projection.exception.ErrorMessages;
import fzs.lab.jpa.projection.exception.RestException;
import fzs.lab.jpa.projection.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        // Ref:1 JPA projection for performance optimization (Only id, username and email needed for each user)
        return userRepository.findByDeletedFalse();
    }

    public User getUserById(Long id) throws RestException.ResourceNotFoundException {
        // Ref:2 Avoided JPA projection for common usage in softDeleteUser(Long id)
        return userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RestException.ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND.getMessage(id)));
    }

    @Transactional
    public User createUser(User user) throws RestException.EmailAlreadyExistsException {
        if (userRepository.existsByEmailAndDeletedFalse(user.getEmail())) {
            throw new RestException.EmailAlreadyExistsException(ErrorMessages.EMAIL_ALREADY_EXISTS.getMessage(user.getEmail()));
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setAge(user.getAge());
        return userRepository.save(newUser);
    }

    @Transactional
    public void softDeleteUser(Long id) throws RestException.ResourceNotFoundException {
        // check if user exist or not
        // Ref:2 Avoided JPA projection in getUserById(id) for common usage
        User user = getUserById(id);
        user.setDeleted(true);
        userRepository.save(user);
    }
}

