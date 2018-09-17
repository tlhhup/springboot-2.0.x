package org.tlh.springboot2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huping
 * @desc
 * @date 18/9/17
 */
@RestController
@RequestMapping("/PropertiesController")
public class PropertiesController {

    @Autowired
    private Environment environment;

    @GetMapping("/name")
    public String name(){
        return this.environment.getProperty("name");
    }

    @GetMapping("/address")
    public String address(){
        return this.environment.getProperty("person.address");
    }

}
