package fcse.soa.users;

import fcse.soa.users.persistence.UserDbEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserDbEntity getByUsername(@RequestParam("user") String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping(value = "/update-balance")
    public void updateUsersBalance(@RequestParam("user") String username, @RequestBody Long newBalance) {
        userService.updateBalanceFor(username, newBalance);
    }
}
