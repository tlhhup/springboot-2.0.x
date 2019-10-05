package org.tlh.postgis.repositories;

import com.vividsolutions.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.tlh.postgis.entity.GlobalPoint;

import java.util.List;

/**
 * Created by 离歌笑tlh/hu ping on 2019/10/5
 * <p>
 * Github: https://github.com/tlhhup
 */
@Repository
public interface GlobalPointRepository extends JpaRepository<GlobalPoint,Integer> {

    //使用原生的sql才能调用函数
    //SELECT name FROM global_points WHERE ST_DWithin(location, 'SRID=4326;POINT(-110 29)'::geography, 1000000);
    @Modifying
    @Query(value = "select * from global_points where ST_DWithin(location,?1,?2)",nativeQuery = true)
    List<GlobalPoint> findDistance(Point point, int distance);
}
