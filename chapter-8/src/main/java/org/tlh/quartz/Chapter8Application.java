package org.tlh.quartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by 离歌笑tlh/hu ping on 2019/1/23
 * <p>
 * Github: https://github.com/tlhhup
 */
@EnableScheduling
@SpringBootApplication
public class Chapter8Application {

    public static void main(String[] args) {
        SpringApplication.run(Chapter8Application.class,args);
    }

}
