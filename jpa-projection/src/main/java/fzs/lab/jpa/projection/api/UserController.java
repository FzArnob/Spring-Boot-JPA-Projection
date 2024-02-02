package fzs.lab.jpa.projection.api;

import fzs.lab.jpa.projection.domain.User;
import fzs.lab.jpa.projection.dto.UserDto;
import fzs.lab.jpa.projection.exception.RestException;
import fzs.lab.jpa.projection.response.RestResponse;
import fzs.lab.jpa.projection.response.SuccessMessages;
import fzs.lab.jpa.projection.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<RestResponse.SuccessResponse> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new RestResponse.SuccessResponse(SuccessMessages.USER_LIST.getMessage(users.size()), users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse.SuccessResponse> getUserById(@PathVariable Long id) throws RestException.ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new RestResponse.SuccessResponse(SuccessMessages.USER_DETAIL.getMessage(id), userService.getUserById(id)));
    }

    @PostMapping
    public ResponseEntity<RestResponse.SuccessResponse> createUser(@RequestBody User user) throws RestException.EmailAlreadyExistsException {
        User newUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new RestResponse.SuccessResponse(SuccessMessages.USER_CREATED.getMessage(newUser.getEmail()), newUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse.SuccessResponse> deleteUser(@PathVariable Long id) throws RestException.ResourceNotFoundException {
        userService.softDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new RestResponse.SuccessResponse(SuccessMessages.USER_DELETED.getMessage(id), null));
    }
}
