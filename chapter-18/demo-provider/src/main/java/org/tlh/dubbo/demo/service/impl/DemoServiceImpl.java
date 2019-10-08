package org.tlh.dubbo.demo.service.impl;


import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;
import org.tlh.dubbo.demo.service.DemoService;

/**
 * Created by 离歌笑tlh/hu ping on 2019/10/8
 * <p>
 * Github: https://github.com/tlhhup
 */
@Service(version = "1.0.0")
public class DemoServiceImpl implements DemoService {

    @Value("${dubbo.application.name}")
    private String applicationName;

    @Override
    public String hello() {
        return this.applicationName;
    }
}
