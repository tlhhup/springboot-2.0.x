package org.tlh.duboo.demo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;
import org.tlh.dubbo.demo.DemoService;

/**
 * Created by 离歌笑tlh/hu ping on 2019/10/8
 * <p>
 * Github: https://github.com/tlhhup
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Value("${dubbo.application.name}")
    private String applicationName;

    @Override
    public String sayHello(String name) {
        return name.concat(this.applicationName);
    }
}
