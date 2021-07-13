package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.UserService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.UserDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.UserDto.UserDtoBuilder;
import ch.fhnw.ip5.praxiscloudservice.api.exception.PraxisIntercomException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.gax.rpc.InvalidArgumentException;
import io.swagger.annotations.Api;
import javassist.tools.web.BadHttpRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
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

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable UUID id){
        return userService.findUserById(id);
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
    public UserDto update(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }


}
