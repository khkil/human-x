package com.example.backend.controller;

import com.example.backend.model.CommonResponse;
import com.example.backend.model.Inspection;
import com.example.backend.model.User;
import com.example.backend.service.InspectionServcice;
import com.example.backend.service.QuestionServcice;
import com.example.backend.service.UserServcice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserServcice userServcice;
    @PostMapping(value = "/insert")
    public ResponseEntity<CommonResponse> insertUser(@RequestBody User user){

        ResponseEntity<CommonResponse> result = new ResponseEntity<CommonResponse>(CommonResponse.successResult(), HttpStatus.OK);
        return result;
    }
}
