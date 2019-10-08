package org.tlh.dubbo.demo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tlh.dubbo.demo.service.DemoService;

/**
 * Created by 离歌笑tlh/hu ping on 2019/10/8
 * <p>
 * Github: https://github.com/tlhhup
 */
@RestController
public class DemoController {

    @Reference(version = "1.0.0")
    private DemoService demoService;

    @GetMapping
    public String hello(){
        return this.demoService.hello();
    }

}
