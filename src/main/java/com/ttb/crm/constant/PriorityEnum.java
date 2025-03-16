package com.ttb.crm.constant;

public enum PriorityEnum {
    LOW,
    MEDIUM,
    HIGH;

    public static PriorityEnum findByKey(String key) {
        try {
            return PriorityEnum.valueOf(key);
        } catch (Exception e) {
            return null;
        }
    }
}
