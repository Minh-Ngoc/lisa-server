package com.example.nhom5.controller;

import com.example.nhom5.domain.User;
import com.example.nhom5.dto.UserDto;
import com.example.nhom5.service.UserService;

import org.hibernate.ResourceClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("users")

public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/list")
    @ResponseBody
    public List<UserDto> getListuser() {
        return userService.getAllUser();
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<User> adduser(@RequestBody User user) {
        user.setRole("user");
        userService.addUser(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<User> updateUser(@RequestBody User userDetails, @PathVariable("id") int userId) {
        User user = userService.findUserById(userId);
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setAddress(userDetails.getAddress());
        user.setPhone(userDetails.getPhone());
        user.setImage(userDetails.getImage());
        //user.setRole(userDetails.getRole());
        user.setUserName(userDetails.getUserName());
        user.setPassWorld(user.getPassWorld());
        User userUpdate = userService.updateUser(user);
        return ResponseEntity.ok(userUpdate);

    }
}
