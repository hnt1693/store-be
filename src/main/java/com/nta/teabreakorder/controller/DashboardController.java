package com.nta.teabreakorder.controller;

import com.nta.teabreakorder.enums.DashboardViewType;
import com.nta.teabreakorder.enums.OrderType;
import com.nta.teabreakorder.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("")
    public ResponseEntity getDashboard(@RequestParam DashboardViewType type) {
        return dashboardService.getDashboardCard(type);
    }

    @GetMapping("/chart")
    public ResponseEntity getChartData(@RequestParam DashboardViewType type, @RequestParam long startTime, @RequestParam long endTime) {
        return dashboardService.getChartData(type, startTime, endTime);
    }
}
