package ch.fhnw.ip6.praxisruf.configuration.web.controller;


import ch.fhnw.ip6.praxisruf.commons.dto.configuration.UserDto;
import ch.fhnw.ip6.praxisruf.configuration.api.UserService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;
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
        return userService.findById(id);
    }

    @GetMapping
    public Set<UserDto> getAllUsers(){
        return userService.findAll();
    }

    @PostMapping()
    public UserDto register(@RequestBody UserDto user) {
        return userService.create(user);
    }

    @PutMapping
    public UserDto update(@RequestBody UserDto userDto) {
        return userService.update(userDto);
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
