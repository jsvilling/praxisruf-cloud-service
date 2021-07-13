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
import java.util.Map;
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

    // ###### Admin CRUD Operations
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable UUID id){
        return userService.findUserById(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers(){
        return userService.findAllUsers();
    }

    @PostMapping()
    public UserDto register(@RequestBody UserDto user) {
        return userService.register(user);
    }

    @PutMapping
    public UserDto update(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id){
        userService.deleteById(id);
    }

    @DeleteMapping("/many/{filter}")
    public void deleteMany(@PathVariable List<UUID> filter){
        userService.deleteAllById(filter);
    }

}
