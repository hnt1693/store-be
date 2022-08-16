package com.nta.teabreakorder.config;

import com.nta.teabreakorder.config.socket.WsAction;
import com.nta.teabreakorder.config.socket.WsManager;
import com.nta.teabreakorder.enums.WsActionType;
import com.nta.teabreakorder.service.DashboardService;
import com.nta.teabreakorder.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.util.Date;

@Configuration
@EnableScheduling
@Slf4j
public class ScheduleConfig {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private JobService jobService;

    //Dashboard process
    @Scheduled(cron = "0 * * * * *")
    public void scheduleTaskUsingCronExpression() throws ParseException {
        log.info("Running summary job at {}", new Date());
        dashboardService.updateDashboardBySummaryJob();
        WsManager.putToAll(new WsAction(WsActionType.SUMMARY, "Updated chart now"));
    }

    //Dashboard process
    @Scheduled(cron = "30 * * * * *")
    public void warningWarhouseLowQuantity() throws Exception {
        log.info("Running warhouse job at {}", new Date());
        jobService.warhouseJob();
        WsManager.putToAll(new WsAction(WsActionType.NEWS, "Updated notify now"));
    }

}
