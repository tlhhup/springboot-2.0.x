package org.tlh.ws.service.impl;

import org.springframework.stereotype.Service;
import org.tlh.ws.service.EchoService;

/**
 * Created by 离歌笑tlh/hu ping on 2019/4/14
 * <p>
 * Github: https://github.com/tlhhup
 */
@Service
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String message) {
        return message;
    }
}
