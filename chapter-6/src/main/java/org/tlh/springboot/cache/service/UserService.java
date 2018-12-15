package org.tlh.springboot.cache.service;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.tlh.springboot.cache.entity.User;

/**
 * Created by 离歌笑tlh/hu ping on 2018/12/15
 * <p>
 * Github: https://github.com/tlhhup
 */
@Slf4j
@Service
public class UserService {

    @Cacheable(value = "auth:user",key = "'id:'+#id")
    public User findUserById(int id){
        log.info("从数据库中获取数据");
        User user=new User();
        user.setAge(12);
        user.setBirthday(DateTime.now().toDate());
        user.setId(id);
        user.setName("张三");
        return user;
    }

}
