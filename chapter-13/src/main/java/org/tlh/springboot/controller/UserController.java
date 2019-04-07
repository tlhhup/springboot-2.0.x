package org.tlh.springboot.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 离歌笑tlh/hu ping on 2019/4/7
 * <p>
 * Github: https://github.com/tlhhup
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public String list(){
        return "user list";
    }

}
