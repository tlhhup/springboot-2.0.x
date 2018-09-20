package org.tlh.springboo2.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.tlh.springboo2.servlet.MyServlet;

/**
 * @author huping
 * @desc
 * @date 18/9/20
 */
@Order(Integer.MAX_VALUE-1)
@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean<MyServlet> servletServletRegistrationBean(){
        ServletRegistrationBean<MyServlet> registrationBean = new ServletRegistrationBean<>(new MyServlet());
        registrationBean.addUrlMappings("/servlet");
        return registrationBean;
    }

}
