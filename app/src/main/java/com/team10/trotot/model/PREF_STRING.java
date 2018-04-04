package com.team10.trotot.model;

/**
 * Created by vinhkhang on 05/12/2017.
 */

public enum PREF_STRING {
    USER_ID("user_id"),
    LOVE_MOTEL_ID("love_motel_id"),
    HISTORY_MOTEL_ID("history_motel_id"),

    NOTIFICATION_STATUS("notification_status"),
    NOTIFICATION_TIME("notification_time"),
    NOTIFICATION_LOCATION_NAME("notification_location_name"),
    NOTIFICATION_LONGITUDE("notification_longitude"),
    NOTIFICATION_LATITUDE("notification_latitude"),
    NOTIFICATION_RADIUS("notification_radius"),
    NOTIFICATION_PRICE_MIN("notification_price_min"),
    NOTIFICATION_PRICE_MAX("notification_price_max"),
    NOTIFICATION_AREA_MIN("notification_area_min"),
    NOTIFICATION_AREA_MAX("notification_area_max"),
    NOTIFICATION_RATING_MIN("notification_rating_min"),
    NOTIFICATION_RATING_MAX("notification_rating_max");

    private String name;

    PREF_STRING(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
