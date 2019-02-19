package org.tlh.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.Test;

/**
 * Created by 离歌笑tlh/hu ping on 2019/2/19
 * <p>
 * Github: https://github.com/tlhhup
 */
public class TextLoggerTest {

    @Test
    public void log(){
        LoggerContext ctx=(LoggerContext) LogManager.getContext();
        Logger file = ctx.getLogger("file");
        file.info("");
        file.info("{}\t{}\t{}",1,2,200);
        file.info("{}\t{}\t{}",1,2,200);
        file.info("{}\t{}\t{}",1,2,200);
        file.info("{}\t{}\t{}",1,2,200);
        file.info("{}\t{}\t{}",1,2,200);
        file.info("{}\t{}\t{}",1,2,200);
        file.info("{}\t{}\t{}",1,2,200);
    }

}
