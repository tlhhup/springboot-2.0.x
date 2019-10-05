package org.tlh.postgis.repositories;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tlh.postgis.PostGisApplication;
import org.tlh.postgis.entity.GlobalPoint;

import java.util.List;

/**
 * Created by 离歌笑tlh/hu ping on 2019/10/5
 * <p>
 * Github: https://github.com/tlhhup
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PostGisApplication.class)
public class GlobalPointRepositoryTest {

    @Autowired
    private GlobalPointRepository globalPointRepository;

    private WKTReader wktReader;

    @Before
    public void before() {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        wktReader = new WKTReader(geometryFactory);
    }

    @Test
    public void save() throws ParseException {
        GlobalPoint globalPoint = new GlobalPoint();
        globalPoint.setName("成都");
        Geometry point = wktReader.read("POINT (117.2 31.8)");
        Point location = point.getInteriorPoint();
        location.setSRID(4326);
        globalPoint.setLocation(location);
        this.globalPointRepository.save(globalPoint);
    }

    @Test
    public void selectOne() {
        List<GlobalPoint> points = this.globalPointRepository.findAll();
        System.out.println(points.size());
    }

    @Test
    public void queryDistance() throws ParseException {
        /**
         * 自动将集合类型的数据转换为地理位置类型数据
         * -- 1.The geography type does not support curves, TINS, or POLYHEDRALSURFACEs, but other geometry types are supported.
         * -- 2.Standard geometry type data will autocast to geography if it is of SRID 4326. 几何类型为postgre内置的数据类型
         */
        Geometry point = wktReader.read("POINT(-110 29)");
        Point location = point.getInteriorPoint();
        location.setSRID(4326);
        List<GlobalPoint> distance = this.globalPointRepository.findDistance(location, 1000000);
        System.out.println(distance.size());
    }

}