package com.nta.teabreakorder.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dashboard {

    private long periodId;

    private int buyCount;

    private int customerCount;

    private int total;

    private int profit;


    public void addBuyCount(int buyCount) {
        this.buyCount += buyCount;
    }

    public void addCustomerCount(int count) {
        this.customerCount += count;
    }

    public void addTotal(int count) {
        this.total += count;
    }

    public void addProfit(int count) {
        this.profit += count;
    }
}
