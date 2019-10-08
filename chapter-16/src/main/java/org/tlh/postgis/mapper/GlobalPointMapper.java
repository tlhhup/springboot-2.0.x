package org.tlh.postgis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.postgis.Point;
import org.tlh.postgis.entity.GlobalPoint;

import java.util.List;

/**
 * Created by 离歌笑tlh/hu ping on 2019/10/6
 * <p>
 * Github: https://github.com/tlhhup
 */
@Mapper
public interface GlobalPointMapper {

    List<GlobalPoint> findByDistance(@Param("adress") Point location,@Param("distance") int distance);

}
