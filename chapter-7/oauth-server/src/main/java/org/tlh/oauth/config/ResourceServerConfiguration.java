package org.tlh.oauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * Created by 离歌笑tlh/hu ping on 2019/1/2
 * <p>
 * Github: https://github.com/tlhhup
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        // 1.todo 该位置为设置有那些资源
        resources.resourceId("order").stateless(true);//.resourceId("product").stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 2. todo 设置那些资源需要授权之后才能访问
        http
                .authorizeRequests()
                .antMatchers("/product/**").authenticated()
                .antMatchers("/order/**").authenticated();//配置order访问控制，必须认证过后才可以访问

    }

}
