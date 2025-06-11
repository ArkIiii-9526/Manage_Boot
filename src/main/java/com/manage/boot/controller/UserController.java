package com.manage.boot.controller;

import com.manage.boot.doto.APIResponse;
import com.manage.boot.doto.LoginRequest;
import com.manage.boot.doto.RegisterRequest;
import com.manage.boot.pojo.User;
import com.manage.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login", produces = "application/json;charset=utf-8")
    public APIResponse<Void> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        if (!userService.isUserExist(email)) {
            return new APIResponse<>(false, "用户不存在", null);
        }

        User user = userService.selectUserByEmail(email);
        String encryptedPwd = DigestUtils.md5DigestAsHex(password.getBytes());

        // 验证密码
        if (!user.getPassword().equals(encryptedPwd)) {
            return new APIResponse<>(false, "密码错误", null);
        }

        return new APIResponse<>(true, "登录成功", null);
    }

    @PostMapping(value = "/register", produces = "application/json;charset=utf-8")
    public APIResponse<Void> register(@RequestBody RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();

        if (userService.isUserExist(email)) {
            return new APIResponse<>(false, "用户已存在", null);
        }

        String encryptedPwd = DigestUtils.md5DigestAsHex(password.getBytes());
        User user = new User();
        user.setEmail(email);
        user.setName(username);
        user.setPassword(encryptedPwd);

        userService.insertUser(user);

        return new APIResponse<>(true, "注册成功", null);
    }
}