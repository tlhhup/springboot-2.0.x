package org.tlh.quartz.service;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

/**
 * Created by 离歌笑tlh/hu ping on 2019/1/23
 * <p>
 * Github: https://github.com/tlhhup
 */
@Service
public class UserService {

    public void analysis(){
        String s = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
        System.out.println(String.format("{%s}分析用户数据",s));
    }

}
