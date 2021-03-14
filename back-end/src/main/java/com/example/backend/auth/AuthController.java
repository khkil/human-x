package com.example.backend.auth;

import com.example.backend.auth.model.Member;
import com.example.backend.common.CommonResponse;
import com.example.backend.test.user.User;
import com.example.backend.test.user.UserServcice;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    MemberService memberService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;



    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Member params){
        Map<String, Object> ret = new HashMap<>();
        Member user = memberService.loadUserByUserName(params.getMember_id());
        if(user == null) {
            return ResponseEntity.ok(CommonResponse.failResult("가입하지 않은 아이디이거나, 잘못된 비밀번호입니다"));
        }
        List<String> roles = new ArrayList<>();
        String token =  jwtTokenProvider.createToken(user.getMember_id(), roles);
        String userPk = jwtTokenProvider.getUserPk(token);

        Member member = memberService.loadUserByUserName(userPk);
        if(!params.getMember_pwd().equals(member.getMember_pwd())){
            return ResponseEntity.ok(CommonResponse.failResult("가입하지 않은 아이디이거나, 잘못된 비밀번호입니다"));
        }
        Jws<Claims> claims = jwtTokenProvider.getClaims(token);
        ret.put("body", claims.getBody());
        ret.put("token", token);
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/info")
    public ResponseEntity getUserInfo(HttpServletRequest request){
        String userPk = (String) request.getAttribute("userPk");
        Member member = memberService.loadUserByUserName(userPk);
        return ResponseEntity.ok().body(member);
    }
}
