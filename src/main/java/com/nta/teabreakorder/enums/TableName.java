package com.nta.teabreakorder.enums;

public enum TableName {
    HOURLY_SUMMARY("hourly_summary"),
    DAILY_SUMMARY("daily_summary"),
    MONTHLY_SUMMARY("monthly_summary"),
    YEARLY_SUMMARY("yearly_summary");
    String label;

    TableName(String label) {
        this.label = label;
    }

    public String getName() {
        return this.label;
    }
}
