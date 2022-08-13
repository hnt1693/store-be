package com.nta.teabreakorder.service.impl;

import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.enums.DashboardViewType;
import com.nta.teabreakorder.enums.OrderType;
import com.nta.teabreakorder.enums.TableName;
import com.nta.teabreakorder.model.Order;
import com.nta.teabreakorder.model.OrderDetail;
import com.nta.teabreakorder.model.Settings;
import com.nta.teabreakorder.payload.response.Dashboard;
import com.nta.teabreakorder.repository.OrderRepository;
import com.nta.teabreakorder.repository.common.SettingRepository;
import com.nta.teabreakorder.repository.dao.DashboardDao;
import com.nta.teabreakorder.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private DashboardDao dashboardDao;

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    final SimpleDateFormat BUSINESS_HOUR_FORMAT = new SimpleDateFormat("HHmm");
    final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final SimpleDateFormat HOURLY_FORMAT = new SimpleDateFormat("yyyyMMddHH");
    final DateTimeFormatter HOURLY_DATEFORMAT = DateTimeFormatter.ofPattern("yyyyMMddHH");
    final SimpleDateFormat DAILY_24 = new SimpleDateFormat("yyyyMMdd24");
    final SimpleDateFormat DAILY_00 = new SimpleDateFormat("yyyyMMdd00");
    final SimpleDateFormat DAILY_FORMAT = new SimpleDateFormat("yyyyMMdd");
    final SimpleDateFormat MONTHLY_01 = new SimpleDateFormat("yyyyMM01");
    final SimpleDateFormat MONTHLY_31 = new SimpleDateFormat("yyyyMM31");
    final SimpleDateFormat MONTHLY_FORMAT = new SimpleDateFormat("yyyyMM");

    @Override
    public ResponseEntity getDashboardCard(DashboardViewType viewType) {

        Calendar endTime = Calendar.getInstance();
        Calendar startTime = Calendar.getInstance();
        SimpleDateFormat HOURLY_FORMAT = new SimpleDateFormat("yyyyMMddHH");
        SimpleDateFormat DAILY_FORMAT = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat MONTHLY_FORMAT = new SimpleDateFormat("yyyyMM");

        endTime.setTime(new Date());
        List<Dashboard> dashboardList = null;

        switch (viewType) {
            case DAILY: {
                startTime.setTime(endTime.getTime());
                startTime.set(Calendar.HOUR_OF_DAY, 0);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.SECOND, 0);
                dashboardList = dashboardDao.getAllSummaryCountByTableNameAndPeriodId(TableName.DAILY_SUMMARY, Long.parseLong(DAILY_FORMAT.format(startTime.getTime())), Long.parseLong(DAILY_FORMAT.format(endTime.getTime())));
                break;
            }
            case WEEKLY: {
                startTime.setTime(endTime.getTime());
                startTime.add(Calendar.DATE, -7);
                break;
            }
            case MONTHLY: {
                startTime.setTime(endTime.getTime());
                startTime.add(Calendar.DATE, 1);
                startTime.add(Calendar.HOUR_OF_DAY, 0);
                startTime.add(Calendar.MINUTE, 0);
                startTime.add(Calendar.SECOND, 0);
                break;
            }
            case YEARLY: {
                startTime.set(Calendar.YEAR, endTime.get(Calendar.YEAR));
                startTime.set(Calendar.MONTH, Calendar.JANUARY);
                startTime.set(Calendar.DAY_OF_MONTH, 1);
                startTime.set(Calendar.HOUR_OF_DAY, 0);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.SECOND, 0);
            }
        }


        Dashboard res = null;
        if (dashboardList.isEmpty()) {
            res = new Dashboard(0, 0, 0, 0, 0);
        } else {
            int buyCount = 0;
            int customerCount = 0;
            int total = 0;
            int profit = 0;
            for (Dashboard dashboard : dashboardList) {
                buyCount += dashboard.getBuyCount();
                total += dashboard.getTotal();
                customerCount += dashboard.getCustomerCount();
                profit += dashboard.getProfit();
            }
            res = new Dashboard(0, buyCount, customerCount, total, profit);
        }
        return CommonUtil.createResponseEntityOK(res);

    }

    @Override
    public ResponseEntity getChartData(DashboardViewType type, long startTime, long endTime) {
        TableName tableName = null;
        switch (type) {
            case HOURLY: {
                tableName = TableName.HOURLY_SUMMARY;
                break;
            }
            case DAILY: {
                tableName = TableName.DAILY_SUMMARY;
                break;
            }
            case WEEKLY: {
                break;
            }
            case MONTHLY: {
                tableName = TableName.MONTHLY_SUMMARY;
                break;
            }
            default: {

            }
        }

        return CommonUtil.createResponseEntityOK(dashboardDao.getAllSummaryCountByTableNameAndPeriodId(tableName, startTime, endTime));
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateDashboardBySummaryJob() throws ParseException {

        Settings summarySetting = settingRepository.findByCode("last_time_summary_job");
        Settings startHourSetting = settingRepository.findByCode("start_hour");
        Settings closeHourSetting = settingRepository.findByCode("close_hour");
        long startHourL = Long.parseLong(startHourSetting.getValue().replace(":", ""));
        long endHourL = Long.parseLong(closeHourSetting.getValue().replace(":", ""));
        Date lastUpdatedDate = TIME_FORMAT.parse(summarySetting.getValue());

        Calendar startDateCalendar = Calendar.getInstance();
        Calendar endDateCalendar = Calendar.getInstance();
        startDateCalendar.setTime(lastUpdatedDate);
        startDateCalendar.set(Calendar.SECOND, 0);
        startDateCalendar.set(Calendar.MINUTE, 0);

        endDateCalendar.setTime(new Date());

        Map<Long, Dashboard> dashboardMap = new HashMap<>();
        List<Order> orderList = orderRepository.getAllByCreatedAtBetween(convertDateToLocalDateTime(startDateCalendar.getTime()), convertDateToLocalDateTime(endDateCalendar.getTime()), OrderType.XUAT);
        //HOURLY SUMMARY
        dashboardDao.deleteSummaryCountByTableNameAndPeriodId(TableName.HOURLY_SUMMARY, Long.parseLong(HOURLY_FORMAT.format(startDateCalendar.getTime())));
        for (Order order : orderList) {
            long periodId = Long.parseLong(order.getCreatedAt().format(HOURLY_DATEFORMAT));
            Dashboard dashboard = null;

            if (dashboardMap.containsKey(periodId)) {
                dashboard = dashboardMap.get(periodId);
            } else {
                dashboard = new Dashboard();
                dashboard.setPeriodId(periodId);
            }
            int profit = 0;
            for (OrderDetail orderDetail : order.getOrderDetailList()) {
                profit += orderDetail.getQuantity() * (orderDetail.getProduct().getPrice() - orderDetail.getProduct().getBasePrice());
            }

            dashboard.addBuyCount(order.getOrderDetailList().size());
            dashboard.addCustomerCount(1);
            dashboard.addTotal(Integer.parseInt(String.valueOf(order.getTotal())));
            dashboard.addProfit(profit);
            dashboardMap.put(periodId, dashboard);

        }
        long startCountL = startDateCalendar.getTimeInMillis();
        Date tempDate = null;
        while (startCountL <= endDateCalendar.getTimeInMillis()) {
            tempDate = new Date(startCountL);
            long periodId = Long.parseLong(HOURLY_FORMAT.format(tempDate));
            long businessPeriod = Long.parseLong(BUSINESS_HOUR_FORMAT.format(tempDate));
            startCountL += (3600 * 1000);
            if (!isInBusinessHour(startHourL, endHourL, businessPeriod)) {
                continue;
            }
            if (!dashboardMap.containsKey(periodId)) {
                dashboardMap.put(periodId, new Dashboard(periodId, 0, 0, 0, 0));
            }

        }

        dashboardDao.insertSummaryCountBy(TableName.HOURLY_SUMMARY, dashboardMap.values());


        //DAILY SUMMARY
        dashboardDao.deleteSummaryCountByTableNameAndPeriodId(TableName.DAILY_SUMMARY, Long.parseLong(DAILY_FORMAT.format(startDateCalendar.getTime())));
        long startTimeL = Long.parseLong(DAILY_00.format(startDateCalendar.getTime()));
        long endTimeL = Long.parseLong(DAILY_24.format(endDateCalendar.getTime()));

        List<Dashboard> dashboardList = dashboardDao.getAllSummaryCountByTableNameAndPeriodId(TableName.HOURLY_SUMMARY, startTimeL, endTimeL);
        if (!dashboardList.isEmpty()) {
            dashboardMap = new HashMap<>();
            for (Dashboard dashboard : dashboardList) {
                long periodId = dashboard.getPeriodId();
                periodId = Long.parseLong(String.valueOf(periodId).substring(0, String.valueOf(periodId).length() - 2));
                Dashboard dashboardInMap = null;
                if (dashboardMap.containsKey(periodId)) {
                    dashboardInMap = dashboardMap.get(periodId);
                } else {
                    dashboardInMap = new Dashboard();
                }
                dashboardInMap.addCustomerCount(dashboard.getCustomerCount());
                dashboardInMap.addProfit(dashboard.getProfit());
                dashboardInMap.addTotal(dashboard.getTotal());
                dashboardInMap.addBuyCount(dashboard.getBuyCount());
                dashboardInMap.setPeriodId(periodId);

                dashboardMap.put(periodId, dashboardInMap);
            }
            dashboardDao.insertSummaryCountBy(TableName.DAILY_SUMMARY, dashboardMap.values());
        }


        //MONTHLY SUMMARY
        dashboardDao.deleteSummaryCountByTableNameAndPeriodId(TableName.MONTHLY_SUMMARY, Long.parseLong(MONTHLY_FORMAT.format(startDateCalendar.getTime())));
        startTimeL = Long.parseLong(MONTHLY_01.format(startDateCalendar.getTime()));
        endTimeL = Long.parseLong(MONTHLY_31.format(endDateCalendar.getTime()));

        dashboardList = dashboardDao.getAllSummaryCountByTableNameAndPeriodId(TableName.DAILY_SUMMARY, startTimeL, endTimeL);

        if (!dashboardList.isEmpty()) {
            dashboardMap = new HashMap<>();
            for (Dashboard dashboard : dashboardList) {
                long periodId = dashboard.getPeriodId();
                periodId = Long.parseLong(String.valueOf(periodId).substring(0, String.valueOf(periodId).length() - 2));

                Dashboard dashboardInMap = null;
                if (dashboardMap.containsKey(periodId)) {
                    dashboardInMap = dashboardMap.get(periodId);
                } else {
                    dashboardInMap = new Dashboard();
                }
                dashboardInMap.addCustomerCount(dashboard.getCustomerCount());
                dashboardInMap.addProfit(dashboard.getProfit());
                dashboardInMap.addTotal(dashboard.getTotal());
                dashboardInMap.addBuyCount(dashboard.getBuyCount());
                dashboardInMap.setPeriodId(periodId);

                dashboardMap.put(periodId, dashboardInMap);
            }
            dashboardDao.insertSummaryCountBy(TableName.MONTHLY_SUMMARY, dashboardMap.values());
        }

        settingRepository.updateValueByKey("last_time_summary_job", TIME_FORMAT.format(endDateCalendar.getTime()));
    }

    private LocalDateTime convertDateToLocalDateTime(Date date) {
        String dateStr = DATE_FORMAT.format(date);
        return LocalDateTime.parse(dateStr);
    }

    private boolean isInBusinessHour(long startTime, long endTime, long now) {
        return startTime <= now && endTime >= now;
    }

}
