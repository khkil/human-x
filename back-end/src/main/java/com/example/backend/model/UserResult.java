package com.example.backend.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UserResult {

    int inspection_idx;
    Map<String, Object> user_info;
    List<Map<String, Integer>> user_answers;
    int user_idx;

}
