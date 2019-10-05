package org.tlh.postgis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by 离歌笑tlh/hu ping on 2019/10/5
 * <p>
 * Github: https://github.com/tlhhup
 */
@SpringBootApplication
@EnableTransactionManagement
public class PostGisApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostGisApplication.class,args);
    }

}
