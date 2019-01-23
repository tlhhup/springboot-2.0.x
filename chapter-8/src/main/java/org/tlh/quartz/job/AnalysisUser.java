package org.tlh.quartz.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.tlh.quartz.service.UserService;

/**
 * Created by 离歌笑tlh/hu ping on 2019/1/23
 * <p>
 * Github: https://github.com/tlhhup
 */
public class AnalysisUser extends QuartzJobBean {

    @Autowired
    private UserService userService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        userService.analysis();
    }
}
