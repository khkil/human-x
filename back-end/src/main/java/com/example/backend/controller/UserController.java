package com.example.backend.controller;

import com.example.backend.config.secutiry.JwtTokenProvider;
import com.example.backend.model.CommonResponse;
import com.example.backend.model.Inspection;
import com.example.backend.model.User;
import com.example.backend.service.InspectionServcice;
import com.example.backend.service.QuestionServcice;
import com.example.backend.service.UserServcice;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserServcice userServcice;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @GetMapping("/count/{inspection_idx}")
    public int getUserCount(@PathVariable int inspection_idx){
        return userServcice.getUserCount(inspection_idx);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> map){
        User user = userServcice.loadUserByUserName(map.get("user_id"));
        if(user == null)
            return new ResponseEntity<CommonResponse>(CommonResponse.failResult("user is null"), HttpStatus.OK);

        List<String> roles = new ArrayList<>();
        String token =  jwtTokenProvider.createToken(user.getUser_id(), roles);
        String userPk = jwtTokenProvider.getUserPk(token);

        User member = userServcice.loadUserByUserName(userPk);
        if(!map.get("user_pwd").equals(member.getUser_pwd()))
            return new ResponseEntity<CommonResponse>(CommonResponse.failResult("user_pwd is incorrectly"), HttpStatus.OK);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestBody Map<String, String> map){
        String token = map.get("token");
        String userPk = jwtTokenProvider.getUserPk(token);
        User user = userServcice.loadUserByUserName(userPk);
        if(user == null)
            return new ResponseEntity<CommonResponse>(CommonResponse.failResult("user is null"), HttpStatus.OK);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
}
