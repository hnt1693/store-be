package com.nta.teabreakorder.enums;

public enum ErrorCode {
    ORDER_ORDERDETAIL_C_QUANTITY("OD030101"),
    ORDER_ORDERDETAIL_U_QUANTITY("OD030301"),
    ORDER_ORDERDETAIL_D_QUANTITY("OD030401"),


    ;






































    String label;

    ErrorCode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
