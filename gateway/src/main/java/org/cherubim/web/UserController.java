package org.cherubim.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    /**
     * 这个是受保护的资源，需要 user.userInfo 权限才可以访问。
     */
    @GetMapping("userInfo")
    public Map<String, Object> userInfo(@AuthenticationPrincipal Jwt principal) {
        return new HashMap<>(4) {{
            put("principal", principal);
            put("userInfo", "获取用户信息");
        }};
    }

    /**
     * 非受权限保护的资源
     */
    @GetMapping("hello")
    public String hello() {
        return "hello 不要需要受保护的资源";
    }

}