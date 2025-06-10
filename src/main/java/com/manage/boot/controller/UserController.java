package com.manage.boot.controller;

import com.manage.boot.doto.APIResponse;
import com.manage.boot.doto.LoginRequest;
import com.manage.boot.doto.RegisterRequest;
import com.manage.boot.pojo.User;
import com.manage.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
    UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping(value = "/login", produces = "application/json;charset=utf-8")
    public ResponseEntity<APIResponse> login(@RequestBody LoginRequest loginRequest){
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        User user = userService.selectUserByEmail(email);
        if(!userService.isUserExist(email)){
            APIResponse response = new APIResponse(false, "用户不存在");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        if (user.getPassword() != password){
            APIResponse response = new APIResponse(false, "密码错误");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
            APIResponse response = new APIResponse(true, "登录成功");
            return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/register", produces = "application/json;charset=utf-8")
    public ResponseEntity<APIResponse> register(@RequestBody RegisterRequest registerRequest){
        String email = registerRequest.getEmail();
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        if (userService.isUserExist(email)){
            APIResponse response = new APIResponse(false, "用户已存在");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        String encryptedPwd = DigestUtils.md5DigestAsHex(password.getBytes());
        User user = new User();
        user.setEmail(email);
        user.setName(username);
        user.setPassword(encryptedPwd);

        userService.insertUser(user);

        APIResponse response = new APIResponse(true, "注册成功");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
