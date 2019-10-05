package org.tlh.postgis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tlh.postgis.entity.Goods;

/**
 * Created by 离歌笑tlh/hu ping on 2019/10/5
 * <p>
 * Github: https://github.com/tlhhup
 */
@Repository
public interface GoodsRepository extends JpaRepository<Goods,Integer> {


}
