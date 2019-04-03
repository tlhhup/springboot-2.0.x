package org.tlh.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 离歌笑tlh/hu ping on 2019/4/3
 * <p>
 * Github: https://github.com/tlhhup
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public String hello(){
        return "hello";
    }

}
