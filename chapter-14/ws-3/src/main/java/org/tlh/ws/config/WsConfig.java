package org.tlh.ws.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;

/**
 * Created by 离歌笑tlh/hu ping on 2019/4/14
 * <p>
 * Github: https://github.com/tlhhup
 */
@Configuration
public class WsConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        //配置对外服务根路径
        return new ServletRegistrationBean(servlet, "/*");
    }

    @Bean
    public SimpleXsdSchema echoXsd() {
        return new SimpleXsdSchema(new ClassPathResource("request/echo.xsd"));
    }

    @Bean
    public DefaultWsdl11Definition echo() {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("Echo");
        definition.setLocationUri("http://localhost:8080/echo/services");
        definition.setSchema(echoXsd());

        return definition;
    }

}
