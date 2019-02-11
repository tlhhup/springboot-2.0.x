package org.tlh.rpc.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tlh.rpc.Chapter9Application;

/**
 * Created by 离歌笑tlh/hu ping on 2019/2/11
 * <p>
 * Github: https://github.com/tlhhup
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Chapter9Application.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void save() throws Exception {
        this.userService.save("哈哈");
    }

}