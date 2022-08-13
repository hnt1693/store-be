package com.nta.teabreakorder.repository.dao;

import com.nta.teabreakorder.enums.TableName;
import com.nta.teabreakorder.payload.response.Dashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class DashboardDao {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void insertSummaryCountBy(TableName table, Collection<Dashboard> list) {
        if (list.size() == 0) return;

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(table.getName());
        sql.append("(customer_count,buy_count,total,profit,period_id) ");
        sql.append(" VALUES");

        for (Dashboard dashboard : list) {
            sql.append("(");
            sql.append(dashboard.getCustomerCount()).append(",");
            sql.append(dashboard.getBuyCount()).append(",");
            sql.append(dashboard.getTotal()).append(",");
            sql.append(dashboard.getProfit()).append(",");
            sql.append(dashboard.getPeriodId()).append("),");
        }
        Query query = entityManager.createNativeQuery(sql.substring(0, sql.length() - 1));
        query.executeUpdate();
    }

    @Transactional
    public void deleteSummaryCountByTableNameAndPeriodId(TableName table, long periodId) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(table.getName());
        sql.append(" WHERE period_id >= ").append(periodId);
        Query query = entityManager.createNativeQuery(sql.toString());
        query.executeUpdate();
    }


    @Transactional
    public List getAllSummaryCountByTableNameAndPeriodId(TableName table, long startTime, long endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT period_id, buy_count, customer_count, total, profit FROM  ");
        sql.append(table.getName());
        sql.append(" WHERE period_id between ").append(startTime);
        sql.append(" AND ").append(endTime);
        Query query = entityManager.createNativeQuery(sql.toString());
        List<Object[]> data = query.getResultList();
        List<Dashboard> dashboardList = new ArrayList<>();
        for (Object[] datum : data) {
            Dashboard dashboard = new Dashboard(
                    Long.parseLong(datum[0].toString()),
                    (int) datum[1],
                    (int) datum[2],
                    (int) datum[3],
                    (int) datum[4]
            );
            dashboardList.add(dashboard);
        }
        return dashboardList;
    }

}
