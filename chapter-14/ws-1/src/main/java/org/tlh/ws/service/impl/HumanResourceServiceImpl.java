package org.tlh.ws.service.impl;

import org.springframework.stereotype.Service;
import org.tlh.ws.service.HumanResourceService;

import java.util.Date;

/**
 * Created by 离歌笑tlh/hu ping on 2019/4/14
 * <p>
 * Github: https://github.com/tlhhup
 */
@Service
public class HumanResourceServiceImpl implements HumanResourceService {

    @Override
    public void bookHoliday(Date startDate, Date endDate, String name) {
        System.out.println("Booking holiday for [" + startDate + "-" + endDate + "] for [" + name + "] ");
    }

}
