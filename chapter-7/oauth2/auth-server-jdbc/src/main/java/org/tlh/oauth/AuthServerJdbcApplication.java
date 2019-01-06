package org.tlh.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by 离歌笑tlh/hu ping on 2019/1/6
 * <p>
 * Github: https://github.com/tlhhup
 */
@SpringBootApplication
@EnableTransactionManagement
public class AuthServerJdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerJdbcApplication.class,args);
    }

}
