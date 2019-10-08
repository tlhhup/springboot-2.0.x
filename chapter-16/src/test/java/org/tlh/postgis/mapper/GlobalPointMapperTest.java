package org.tlh.postgis.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.postgis.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tlh.postgis.entity.GlobalPoint;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by 离歌笑tlh/hu ping on 2019/10/6
 * <p>
 * Github: https://github.com/tlhhup
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GlobalPointMapperTest {

    @Autowired
    private GlobalPointMapper globalPointMapper;

    @Test
    public void findByDistance() {
        Point point=new Point(-110 ,29);
        List<GlobalPoint> points = this.globalPointMapper.findByDistance(point, 1000000);
        System.out.println(points.size());
    }
}