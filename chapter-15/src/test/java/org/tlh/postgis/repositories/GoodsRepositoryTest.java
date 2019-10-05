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
import org.tlh.postgis.entity.Goods;

import java.util.List;

/**
 * Created by 离歌笑tlh/hu ping on 2019/10/5
 * <p>
 * Github: https://github.com/tlhhup
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PostGisApplication.class)
public class GoodsRepositoryTest {

    @Autowired
    private GoodsRepository goodsRepository;

    private WKTReader wktReader;

    @Before
    public void before(){
        GeometryFactory geometryFactory=new GeometryFactory(new PrecisionModel(),4326);
        wktReader = new WKTReader(geometryFactory);
    }

    @Test
    public void save() throws ParseException {
        Goods goods=new Goods();
        goods.setName("成都");
        //需要通过改对象来创建
        Geometry point = wktReader.read("POINT (117.2 31.8)");
        Point location = point.getInteriorPoint();
        //设置坐标系
        location.setSRID(4326);

        goods.setLocation(location);

        this.goodsRepository.save(goods);
    }

    @Test
    public void selectOne(){
        List<Goods> points = this.goodsRepository.findAll();
        System.out.println(points.size());
    }

}