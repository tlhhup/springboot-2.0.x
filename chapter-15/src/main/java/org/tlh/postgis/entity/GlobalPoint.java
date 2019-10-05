package org.tlh.postgis.entity;


import com.vividsolutions.jts.geom.Point;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by 离歌笑tlh/hu ping on 2019/10/5
 * <p>
 * Github: https://github.com/tlhhup
 */
@Data
@Entity
@Table(name = "global_points")
public class GlobalPoint implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    //自动创建表，定义字段的创建语法
    @Column(columnDefinition = "geography(Point,4326)")
    private Point location;


}
