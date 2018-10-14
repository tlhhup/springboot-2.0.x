package org.tlh.springboot.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author huping
 * @desc
 * @date 18/10/14
 */
@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer age;

}
