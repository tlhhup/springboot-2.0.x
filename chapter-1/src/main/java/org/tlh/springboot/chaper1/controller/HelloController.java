package org.tlh.springboot.chaper1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huping
 * @desc
 * @date 18/9/16
 */
@RestController
public class HelloController {

    @RequestMapping("/")
    public String hello(){
        return "hello,spring boot!!";
    }

    @RequestMapping("/restart")
    public String devTools(){
        return "devTools restart!!";
    }
}
