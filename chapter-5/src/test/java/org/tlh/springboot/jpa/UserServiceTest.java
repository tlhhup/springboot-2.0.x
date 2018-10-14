package org.tlh.springboot.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tlh.springboot.jpa.entity.User;
import org.tlh.springboot.jpa.service.UserService;

/**
 * @author huping
 * @desc
 * @date 18/10/14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void save() {
        User user = new User();
        user.setAge(15);
        user.setName("list211231");
        this.userService.save(user);
    }

}
