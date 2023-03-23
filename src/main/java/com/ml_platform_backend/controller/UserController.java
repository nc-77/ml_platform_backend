package com.ml_platform_backend.controller;

import com.ml_platform_backend.entry.User;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import com.ml_platform_backend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Data
    static class Req {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    static class Resp {
        private Integer id;
        private String username;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getUserById(@PathVariable Integer id) {
        User user = userService.getUserByid(id);
        return new ResponseEntity(Code.SUCCESS.getValue(), new Resp(user.getId(), user.getUsername()), Code.SUCCESS.getDescription());
    }

    @PostMapping("/user/register")
    public ResponseEntity register(@RequestBody Req req) {
        User user = new User(req.username, req.password);
        try {
            user = userService.register(user);
        } catch (Exception e) {
            return new ResponseEntity(Code.FAILED.getValue(), null, e.getMessage());
        }
        return new ResponseEntity(Code.SUCCESS.getValue(), new Resp(user.getId(), user.getUsername()), Code.SUCCESS.getDescription());
    }

    @PostMapping("/user/login")
    public ResponseEntity login(@RequestBody Req req) {
        User user = new User(req.username, req.password);
        User loginUser = userService.login(user);
        if (loginUser == null) {
            return new ResponseEntity(Code.LOGIN_ERR.getValue(), null, Code.LOGIN_ERR.getDescription());
        }
        return new ResponseEntity(Code.LOGIN_OK.getValue(), new Resp(loginUser.getId(), loginUser.getUsername()), Code.LOGIN_OK.getDescription());
    }
}
