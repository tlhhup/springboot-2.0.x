package org.tlh.ws.test;

import org.junit.Test;
import org.tlh.ws.client.HolidayClient;

import static org.junit.Assert.*;

/**
 * Created by 离歌笑tlh/hu ping on 2019/4/14
 * <p>
 * Github: https://github.com/tlhhup
 */
public class HolidayClientTest {

    @Test
    public void sendHolidayRequest() {
        HolidayClient holidayClient=new HolidayClient();
        holidayClient.sendHolidayRequest();
    }
}