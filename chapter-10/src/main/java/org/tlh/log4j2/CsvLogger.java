package org.tlh.log4j2;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.message.ObjectArrayMessage;

/**
 * Created by 离歌笑tlh/hu ping on 2019/2/19
 * <p>
 * Github: https://github.com/tlhhup
 */
@Log4j2
public class CsvLogger {

    public void save(){
        log.info("");
        log.info(new ObjectArrayMessage(1,2,200));
        log.info(new ObjectArrayMessage(1,2,200));
        log.info(new ObjectArrayMessage(1,2,200));
        log.info(new ObjectArrayMessage(1,2,200));
        log.info(new ObjectArrayMessage(1,2,200));
        log.info(new ObjectArrayMessage(1,2,200));
    }

}
