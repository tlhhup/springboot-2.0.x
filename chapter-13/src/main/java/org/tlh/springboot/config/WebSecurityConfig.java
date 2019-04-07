package org.tlh.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Created by 离歌笑tlh/hu ping on 2019/4/7
 * <p>
 * Github: https://github.com/tlhhup
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        //设置用户数据
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withDefaultPasswordEncoder().username("admin").password("admin").roles("USER").build());
        manager.createUser(User.withDefaultPasswordEncoder().username("test").password("admin").roles("TEST").build());
        manager.createUser(User.withDefaultPasswordEncoder().username("test1").password("admin").roles("TEST","USER").build());
        return manager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 设置认证规则
        http.authorizeRequests()//设置权限控制规则
            .anyRequest().authenticated()//所有请求都需要认证
            .and()
            .formLogin()//设置表单登陆
            .loginPage("/customLogin")//设置登陆界面及处理登陆请求、退出的URL地址
            //.successForwardUrl("/index")//设置之后，授权成功之后会通过转发的方式实现跳转，请求方式还是为POST，所以需要编写一个controller进行特殊处理
            .permitAll()//
            .and()
            .csrf()
            .disable();
    }
}
