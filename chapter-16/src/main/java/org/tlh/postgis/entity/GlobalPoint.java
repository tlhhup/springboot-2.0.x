package org.tlh.postgis.entity;

import lombok.Data;
import org.postgis.Point;

import java.io.Serializable;

/**
 * Created by 离歌笑tlh/hu ping on 2019/10/6
 * <p>
 * Github: https://github.com/tlhhup
 */
@Data
public class GlobalPoint implements Serializable {

    private Integer id;

    private String name;

    private Point location;

}
