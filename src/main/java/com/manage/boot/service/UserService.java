package com.manage.boot.service;

import com.manage.boot.pojo.User;

public interface UserService {

    User selectUserByEmail(String email);

    int insertUser(User user);

    boolean isUserExist(String email);


}
