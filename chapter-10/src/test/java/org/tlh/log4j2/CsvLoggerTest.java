package org.tlh.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.message.ObjectArrayMessage;
import org.junit.Test;

/**
 * Created by 离歌笑tlh/hu ping on 2019/2/19
 * <p>
 * Github: https://github.com/tlhhup
 */
public class CsvLoggerTest {

    @Test
    public void log(){
        LoggerContext ctx=(LoggerContext) LogManager.getContext();
        Logger csv = ctx.getLogger("csv");
        //处理头没有换行问题
        csv.info("");
        csv.info(new ObjectArrayMessage(1,2,200));
        csv.info(new ObjectArrayMessage(1,2,200));
        csv.info(new ObjectArrayMessage(1,2,200));
        csv.info(new ObjectArrayMessage(1,2,200));
        csv.info(new ObjectArrayMessage(1,2,200));
    }

}
