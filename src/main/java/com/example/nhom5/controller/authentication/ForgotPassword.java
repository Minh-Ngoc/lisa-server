package com.example.nhom5.controller.authentication;

import com.example.nhom5.domain.User;
import com.example.nhom5.model.RegisterRequestDto;
import com.example.nhom5.model.RegisterResponseDto;
import com.example.nhom5.service.UserService;
import com.example.nhom5.utils.UtilsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/reset-password")
@CrossOrigin("http://localhost:3000/")
public class ForgotPassword {

    @Autowired
    private UtilsService utilsService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping
    public ResponseEntity<RegisterResponseDto> forgotPassword(@RequestBody RegisterRequestDto registerRequest,
                                                              HttpServletResponse response) {
        // user entity
        User user = userService.findByUsername(registerRequest.getUsername());
        System.out.println("USER: " + user);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RegisterResponseDto("User not found", "",
                    "", "USER_NOT_FOUND","",user.getUserId()));
        } else {
            // if user exists
            try {
                user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
                String token = UtilsService.getRandomHexString(150);
                user.setToken(token);
                userService.updateTokenById(token, user.getUserId());
                // Set cookie
                Cookie cookie = new Cookie("token", token);
                cookie.setMaxAge(3600);
                //add cookie to response
                response.addCookie(cookie);

                return ResponseEntity.status(HttpStatus.OK).body(new RegisterResponseDto("Reset Password Successfully",
                        "", "",
                        "RESET_PASSWORD_SUCCESSFULLY","",user.getUserId()));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponseDto("Unknown Error", "",
                        e.getMessage(), "UNKNOWN_ERROR","",user.getUserId()));
            }
        }
    }
}