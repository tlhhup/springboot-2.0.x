package org.tlh.springboot.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author huping
 * @desc
 * @date 18/10/14
 */
@EnableJpaRepositories
@SpringBootApplication
@EnableTransactionManagement
public class Chapter5Application {

    public static void main(String[] args) {
        SpringApplication.run(Chapter5Application.class,args);
    }

}
