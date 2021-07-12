package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.UserService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.UserDto;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Api(tags = "Users")
public class UsersController {
    private final UserService userService;

    //Login Endpoint
    @GetMapping("/login")
    public Principal user(Principal user){
        return user;
    }

    @PostMapping("/register")
    public UUID register(String userName, String password, String role) {
        return userService.register(UserDto.builder().userName(userName).password(password).role(role).build());
    }

    @GetMapping
    public List<UserDto> getAllUsers(){
        return userService.findAllUsers();
    }

    @PutMapping
    public UUID update(UserDto user){
        return userService.updateUser(user);
    }


}
