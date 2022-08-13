package com.nta.teabreakorder.service;

import com.nta.teabreakorder.enums.DashboardViewType;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public interface DashboardService {

    ResponseEntity getDashboardCard(DashboardViewType viewType);

    ResponseEntity getChartData(DashboardViewType type,long startTime, long endTime);

    void updateDashboardBySummaryJob() throws ParseException;
}
