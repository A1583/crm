package com.ttb.crm.constant;

public enum CustomerRequestStatusEnum {
    IN_PROGRESS,
    COMPLETED,
    CANCELED,
    ;

    public static CustomerRequestStatusEnum findByKey(String key) {
        try {
            return CustomerRequestStatusEnum.valueOf(key);
        } catch (Exception e) {
            return null;
        }
    }
}
