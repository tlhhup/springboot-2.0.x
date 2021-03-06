package org.tlh.ws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;

/**
 * Created by 离歌笑tlh/hu ping on 2019/4/14
 * <p>
 * Github: https://github.com/tlhhup
 */
@Configuration
public class EchoConfig {

    @Bean
    public SimpleXsdSchema echoXsd() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/echo.xsd"));
    }

    @Bean
    public DefaultWsdl11Definition echo() {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("Echo");
        definition.setLocationUri("/echo/services");
        definition.setSchema(echoXsd());

        return definition;
    }

}
