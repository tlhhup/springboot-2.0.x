package org.tlh.log4j2;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

/**
 * Created by 离歌笑tlh/hu ping on 2019/2/19
 * <p>
 * Github: https://github.com/tlhhup
 */
@Log4j2
public class LombokTest {

    @Test
    public void log(){
        log.info("hhh");
    }

}
