package com.brunodias.pixTest.controllers;

import com.brunodias.pixTest.dtos.users.RegisterUserRequest;
import com.brunodias.pixTest.dtos.users.RegisterUserResponse;
import com.brunodias.pixTest.entities.User;
import com.brunodias.pixTest.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> registerUser(@RequestBody @Valid RegisterUserRequest request) throws MessagingException, UnsupportedEncodingException
    {
        var user = request.toModel();
        var response = this.userService.registerUser(user);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code){
        if(userService.verify(code)){
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }
}
