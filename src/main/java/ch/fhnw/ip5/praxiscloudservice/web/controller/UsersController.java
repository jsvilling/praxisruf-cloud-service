package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.UserService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.UserDto;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Api(tags = "Users")
public class UsersController {
    private final UserService userService;

    @PostMapping("/register")
    public UUID register(String userName) {
        return userService.register(userName);
    }

    @GetMapping
    public List<UserDto> getAllUsers(){
        return userService.findAllUsers();
    }

}
