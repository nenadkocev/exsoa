package fcse.soa.users;

import fcse.soa.users.persistence.UserDbEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserDbEntity getByUsername(@RequestParam("user") String username) {
        return userService.getUserByUsername(username);
    }
}
