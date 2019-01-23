package org.tlh.quartz.config;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tlh.quartz.job.AnalysisUser;

/**
 * Created by 离歌笑tlh/hu ping on 2019/1/23
 * <p>
 * Github: https://github.com/tlhhup
 */
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail analysisUserJob(){
        return JobBuilder.newJob().ofType(AnalysisUser.class)
                .withIdentity("analysisUserJob")
                .withDescription("分析用户数据")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger analysisUserTrigger(){
        return TriggerBuilder.newTrigger()
                .forJob(analysisUserJob())
                .withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * ? * *"))
                .withIdentity("analysisUserTrigger")
                .withDescription("分析用户数据")
                .build();
    }

}
