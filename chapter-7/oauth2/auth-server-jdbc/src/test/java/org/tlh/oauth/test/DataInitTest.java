package org.tlh.oauth.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.tlh.oauth.AuthServerJdbcApplication;

import java.util.Arrays;

/**
 * Created by 离歌笑tlh/hu ping on 2019/1/6
 * <p>
 * Github: https://github.com/tlhhup
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthServerJdbcApplication.class)
public class DataInitTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertUser() {
        String sql = "insert into users(username,password,realname,usertype,enabled) values(?,?,?,?,?)";
        this.jdbcTemplate.batchUpdate(sql, Arrays.asList(new Object[]{
                        "user_1",
                        passwordEncoder.encode("123456"),
                        "离歌笑",
                        1,
                        true
                }, new Object[]{
                        "user_2",
                        passwordEncoder.encode("123456"),
                        "离歌笑2",
                        1,
                        true
                }
        ));
    }

    @Test
    public void insertClient() {
        String sql = "insert into oauth_client_details (client_secret,resource_ids,authorized_grant_types,client_id,scope,authorities) values (?,?,?,?,?,?)";
        this.jdbcTemplate.batchUpdate(sql, Arrays.asList(new Object[]{
                        passwordEncoder.encode("123456"),
                        "order",
                        "client_credentials,refresh_token",
                        "client_1",
                        "select",
                        "oauth2"
                }, new Object[]{
                        passwordEncoder.encode("123456"),
                        "order,product",
                        "password,refresh_token",
                        "client_2",
                        "select",
                        "oauth2"
                }, new Object[]{
                        passwordEncoder.encode("123456"),
                        null,
                        null,
                        "client",
                        null,
                        null
                }
        ));
    }

}
