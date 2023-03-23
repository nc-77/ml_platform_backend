package com.ml_platform_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ml_platform_backend.entry.User;
import com.ml_platform_backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public int insertUser(User user) {
        return userMapper.insert(user);
    }

    public User getUserByid(Integer userId) {
        return userMapper.selectById(userId);
    }

    public User register(User user) throws Exception {
        // 检查username唯一性
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", user.getUsername());
        if (userMapper.exists(query)) {
            throw new Exception("用户名已存在");
        }
        insertUser(user);
        return user;
    }

    public User login(User user) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.and(i -> i.eq("username", user.getUsername()).eq("password", user.getPassword()));
        return userMapper.selectOne(query);
    }
}
