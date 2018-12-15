package org.tlh.springboot.cache.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 离歌笑tlh/hu ping on 2018/12/15
 * <p>
 * Github: https://github.com/tlhhup
 */
@Data
public class User implements Serializable{

    private int id;
    private String name;
    private Date birthday;
    private int age;

}
