package ch.fhnw.ip5.praxiscloudservice.web.controller;

import ch.fhnw.ip5.praxiscloudservice.api.UserService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Api(tags = "Authentication")
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public UUID login(String userName) {
        return userService.login(userName);
    }

    @PostMapping("/logout")
    public void logout(String userName) {
        userService.logout(userName);
    }

}
