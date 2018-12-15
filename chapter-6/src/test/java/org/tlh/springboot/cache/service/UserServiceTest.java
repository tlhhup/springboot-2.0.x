package org.tlh.springboot.cache.service;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 离歌笑tlh/hu ping on 2018/12/15
 * <p>
 * Github: https://github.com/tlhhup
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void findUserById() throws Exception {
        this.userService.findUserById(1);
        log.info("*******************");
        this.userService.findUserById(1);
    }

}
