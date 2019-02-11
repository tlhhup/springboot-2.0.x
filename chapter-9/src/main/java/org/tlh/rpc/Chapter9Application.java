package org.tlh.rpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tlh.rpc.core.EnableRpcClients;

/**
 * Created by 离歌笑tlh/hu ping on 2019/2/11
 * <p>
 * Github: https://github.com/tlhhup
 */
@EnableRpcClients(basePackages = "org.tlh.rpc.service")
@SpringBootApplication
public class Chapter9Application {

    public static void main(String[] args) {
        SpringApplication.run(Chapter9Application.class,args);
    }

}
